package br.com.imsa.easyfood.api.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginRequest", description = "Credentials used to authenticate a user")
public class LoginRequest {

    @Schema(description = "Username for login", example = "jdoe")
    @NotBlank(message = "{validation.login.username.required}")
    private String username;

    @Schema(description = "Password for login", example = "Str0ngP@ss!")
    @NotBlank(message = "{validation.login.password.required}")
    private String password;
}
