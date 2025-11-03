package br.com.imsa.easyfood.api.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginResponse", description = "Response returned after successful authentication")
public class LoginResponse {

    @Schema(description = "JWT access token")
    private String token;

    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Authenticated username", example = "jdoe")
    private String username;

    @Schema(description = "Token expiration duration in milliseconds", example = "86400000")
    private Integer tokenExpiryDuration;

}
