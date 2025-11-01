package br.com.imsa.easyfood.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Por favor, informe seu usuário.")
    private String username;

    @NotBlank(message = "Por favor, informe sua senha.")
    private String password;
}
