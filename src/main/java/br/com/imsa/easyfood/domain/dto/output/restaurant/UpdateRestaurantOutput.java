package br.com.imsa.easyfood.domain.dto.output.restaurant;

import br.com.imsa.easyfood.domain.dto.output.address.UpdateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.UpdateUserSystemOutput;

import java.time.LocalDateTime;

public record UpdateRestaurantOutput(
        Long id,
        String name,
        UpdateAddressOutput address,
        String kitchenType,
        LocalDateTime startOperationTime,
        LocalDateTime endOperationTime,
        UpdateUserSystemOutput proprietary
) {
}
