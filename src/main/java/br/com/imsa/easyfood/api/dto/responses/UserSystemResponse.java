package br.com.imsa.easyfood.api.dto.responses;

import br.com.imsa.easyfood.domain.enums.UserTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserSystemResponse", description = "User data returned by the API")
public class UserSystemResponse {

    @Schema(description = "User identifier", example = "1")
    private Long id;

    @Schema(description = "Unique username", example = "jdoe")
    private String username;

    @Schema(description = "User email", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Full name", example = "John Doe")
    private String name;

    @Schema(description = "User address")
    private AddressResponse address;

    @Schema(description = "User type acronym", example = "CLIENT")
    private UserTypeEnum userType;

    @Schema(description = "Creation Date", example = "2025-11-02T22:26:10")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createAt;

    @Schema(description = "Last Modification Date", example = "2025-11-02T22:26:10")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateAt;
}
