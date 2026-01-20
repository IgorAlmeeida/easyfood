package br.com.imsa.easyfood.application.v1.dto.requests;

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
@Schema(name = "UserTypeCreateRequest", description = "Request payload to create a user type")
public class UserTypeCreateRequest {

    @NotBlank
    private String name;
}
