package br.com.imsa.easyfood.api.controller.v1;

import br.com.imsa.easyfood.api.dto.requests.ChangePasswordRequest;
import br.com.imsa.easyfood.api.dto.requests.LoginRequest;
import br.com.imsa.easyfood.api.dto.responses.LoginResponse;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.provider.TokenProvider;
import br.com.imsa.easyfood.domain.service.UserSystemPasswordService;
import br.com.imsa.easyfood.exception.NegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping(value = "/api/auth/v1", produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and password management")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserSystemPasswordService userSystemPasswordService;
    private final TokenProvider tokenProvider;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    public ResponseEntity<LoginResponse> loginUserSystem(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            throw new NegocioException("Usuário ou senha inválidas");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserSystem userSystem = (UserSystem) authentication.getPrincipal();

        return ResponseEntity.ok(new LoginResponse(
                tokenProvider.generate(authentication),
                "Bearer",
                userSystem.getUsername(),
                jwtExpirationMs));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Changes the password of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSystem userSystem = (UserSystem) authentication.getPrincipal();
        this.userSystemPasswordService.changePassword(userSystem.getId(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
