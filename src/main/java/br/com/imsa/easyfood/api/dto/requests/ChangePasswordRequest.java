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
public class ChangePasswordRequest {

    @NotBlank(message = "Por favor, informe sua senha atual.")
    private String oldPassword;

    @NotBlank(message = "Por favor, informe sua senha nova senha.")
    private String newPassword;
}
