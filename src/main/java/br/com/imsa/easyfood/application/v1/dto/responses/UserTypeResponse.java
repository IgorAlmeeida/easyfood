package br.com.imsa.easyfood.application.v1.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserTypeResponse", description = "User type API response")
public class UserTypeResponse {
    private Long id;
    private String name;
}
