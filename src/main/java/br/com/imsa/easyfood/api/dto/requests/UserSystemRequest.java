package br.com.imsa.easyfood.api.dto.requests;

import br.com.imsa.easyfood.domain.enums.UserTypeEnum;
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
public class UserSystemRequest {

    @NotBlank(message = "É necessário informar o username.")
    private String username;

    @NotBlank(message = "É necessário informar o nome do usuário.")
    private String name;

    @NotBlank(message = "É necessário informar o email.")
    private String email;

    @NotNull(message = "É necessário informar o tipo do usuário.")
    private UserTypeEnum userType;

    @NotBlank(message = "É necessário informar a senha do usuário.")
    private String password;

    @NotNull(message = "É necessário informar a senha do usuário.")
    private AddressRequest address;


}
