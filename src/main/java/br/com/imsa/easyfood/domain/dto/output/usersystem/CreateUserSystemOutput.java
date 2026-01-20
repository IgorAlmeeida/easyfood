package br.com.imsa.easyfood.domain.dto.output.usersystem;

import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.usertype.CreateUserTypeOutput;
import br.com.imsa.easyfood.domain.enums.UserTypeEnum;

public record CreateUserSystemOutput(
        Long id,
        String username,
        String name,
        String email,
        CreateUserTypeOutput userType,
        boolean active,
        CreateAddressOutput address
) {
}
