package br.com.imsa.easyfood.domain.dto.input.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.infra.enums.UserTypeEnum;

public record CreateUserSystemInput(
        String username,
        String name,
        String email,
        UserTypeEnum userType,
        CreateAddressInput address,
        String password
) {
}
