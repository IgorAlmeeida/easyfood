package br.com.imsa.easyfood.domain.dto.output.restaurant;

import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.CreateUserSystemOutput;

import java.time.LocalDateTime;

public record CreateRestaurantOutput(
        Long id,
        String name,
        CreateAddressOutput address,
        String kitchenType,
        LocalDateTime startOperationTime,
        LocalDateTime endOperationTime,
        CreateUserSystemOutput proprietary
) {
}
