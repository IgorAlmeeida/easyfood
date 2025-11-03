package br.com.imsa.easyfood.api.dto.requests;

import br.com.imsa.easyfood.domain.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserSystemRequest", description = "Payload to create or update a system user")
public class UserSystemUpdateRequest {

    @Schema(description = "Unique username for login", example = "jdoe")
    @NotBlank(message = "{validation.user.username.required}")
    private String username;

    @Schema(description = "Full name of the user", example = "John Doe")
    @NotBlank(message = "{validation.user.name.required}")
    private String name;

    @Schema(description = "User email", example = "john.doe@example.com")
    @Email(message = "{validation.user.email.required}")
    @NotNull(message = "{validation.user.type.required}")
    private String email;

    @Schema(description = "Type of user", example = "CLIENT")
    @NotNull(message = "{validation.user.type.required}")
    private UserTypeEnum userType;

    @Schema(description = "Account password", example = "Str0ngP@ss!")
    @NotBlank(message = "{validation.user.password.required}")
    private String password;

    @Schema(description = "User address")
    @NotNull(message = "{validation.user.address.required}")
    private AddressRequest address;


}
