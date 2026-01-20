package br.com.imsa.easyfood.domain.dto.output.usersystem;

import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.infra.enums.UserTypeEnum;

public record CreateUserSystemOutput(
        Long id,
        String username,
        String name,
        String email,
        UserTypeEnum userType,
        boolean active,
        CreateAddressOutput address
) {
}
