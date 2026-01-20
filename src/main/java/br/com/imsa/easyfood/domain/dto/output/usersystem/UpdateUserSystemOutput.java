package br.com.imsa.easyfood.domain.dto.output.usersystem;

import br.com.imsa.easyfood.domain.dto.output.address.UpdateAddressOutput;
import br.com.imsa.easyfood.infra.enums.UserTypeEnum;

public record UpdateUserSystemOutput(
        Long id,
        String username,
        String name,
        String email,
        UserTypeEnum userType,
        boolean active,
        UpdateAddressOutput address
) {
}
