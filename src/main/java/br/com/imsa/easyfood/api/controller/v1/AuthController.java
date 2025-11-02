package br.com.imsa.easyfood.api.controller.v1;

import br.com.imsa.easyfood.api.dto.requests.ChangePasswordRequest;
import br.com.imsa.easyfood.api.dto.requests.LoginRequest;
import br.com.imsa.easyfood.api.dto.responses.LoginResponse;
import br.com.imsa.easyfood.config.jwt.JwtUtils;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.service.UserSystemService;
import br.com.imsa.easyfood.exception.NegocioException;
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
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserSystemService userSystemService;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUserSystem(@RequestBody LoginRequest loginRequest) {
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
                jwtUtils.generateJwtToken(authentication),
                "Bearer",
                userSystem.getUsername(),
                jwtExpirationMs));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSystem userSystem = (UserSystem) authentication.getPrincipal();
        this.userSystemService.changePassword(userSystem.getId(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
