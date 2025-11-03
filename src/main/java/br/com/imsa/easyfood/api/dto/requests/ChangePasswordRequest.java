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
@Schema(name = "ChangePasswordRequest", description = "Payload to change the current user's password")
public class ChangePasswordRequest {

    @Schema(description = "Current password", example = "OldP@ss123")
    @NotBlank(message = "{validation.password.old.required}")
    private String oldPassword;

    @Schema(description = "New password", example = "NewP@ss456")
    @NotBlank(message = "{validation.password.new.required}")
    private String newPassword;
}
