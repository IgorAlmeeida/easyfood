package br.com.imsa.easyfood.domain.dto.output.usersystem;

import br.com.imsa.easyfood.domain.dto.output.address.UpdateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.usertype.UpdateUserTypeOutput;
import br.com.imsa.easyfood.domain.enums.UserTypeEnum;

public record UpdateUserSystemOutput(
        Long id,
        String username,
        String name,
        String email,
        UpdateUserTypeOutput userType,
        boolean active,
        UpdateAddressOutput address
) {
}
