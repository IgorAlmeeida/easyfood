package br.com.imsa.easyfood.domain.dto.input.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.enums.UserTypeEnum;

public record UpdateUserSystemInput(
        Long id,
        String username,
        String name,
        String email,
        Long userType,
        Boolean active,
        UpdateAddressInput address
) {
}
