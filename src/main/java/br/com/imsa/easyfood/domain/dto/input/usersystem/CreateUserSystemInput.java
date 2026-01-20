package br.com.imsa.easyfood.domain.dto.input.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.enums.UserTypeEnum;

public record CreateUserSystemInput(
        String username,
        String name,
        String email,
        Long userType,
        CreateAddressInput address,
        String password
) {
}
