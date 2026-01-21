package br.com.imsa.easyfood.application.v1.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserTypeUpdateRequest", description = "Request payload to update a user type")
public class UserTypeUpdateRequest {
    @Schema(description = "User type name", example = "CUSTOMER")
    private String name;
}
