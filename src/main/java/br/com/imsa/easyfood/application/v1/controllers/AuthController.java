package br.com.imsa.easyfood.application.v1.controllers;

import br.com.imsa.easyfood.application.v1.dto.requests.ChangePasswordRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.LoginRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.LoginResponse;
import br.com.imsa.easyfood.domain.dto.output.auth.LoginOutput;
import br.com.imsa.easyfood.domain.usercase.auth.ChangePasswordUserCase;
import br.com.imsa.easyfood.domain.usercase.auth.LoginUserCase;
import br.com.imsa.easyfood.infra.exception.ErrorResponse;
import br.com.imsa.easyfood.infra.adpter.AuthEntityRepository;
import br.com.imsa.easyfood.infra.adpter.UserSystemEntityRepository;
import br.com.imsa.easyfood.infra.mappers.UserSystemMapper;
import br.com.imsa.easyfood.infra.provider.TokenProvider;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth/v1", produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and password management")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;
    private final TokenProvider tokenProvider;
    private final UserSystemRepository userSystemRepository;
    private final UserSystemMapper userSystemMapper;
    private final PasswordEncoder passwordEncoder;


    private AuthEntityRepository authGateway() {
        return new AuthEntityRepository(authenticationManager, tokenProvider, messageSource, userSystemRepository);
    }

    private UserSystemEntityRepository userSystemGateway() {
        return new UserSystemEntityRepository(userSystemRepository, userSystemMapper, passwordEncoder);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoginResponse> loginUserSystem(@Valid @RequestBody LoginRequest loginRequest) {
        LoginUserCase useCase = new LoginUserCase(authGateway());
        LoginOutput output = useCase.execute(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new LoginResponse(
                output.token(),
                output.type(),
                output.username(),
                output.tokenExpiryDuration()
        ));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Changes the password of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : null;
        if (username == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ChangePasswordUserCase useCase = new ChangePasswordUserCase(userSystemGateway());
        userSystemGateway().findByUsername(username).ifPresent(user ->
                useCase.execute(user.getId(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword())
        );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
