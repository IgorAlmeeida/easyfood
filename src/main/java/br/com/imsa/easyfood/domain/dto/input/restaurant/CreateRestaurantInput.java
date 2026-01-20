package br.com.imsa.easyfood.domain.dto.input.restaurant;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;

import java.time.LocalDateTime;

public record CreateRestaurantInput(
        String name,
        CreateAddressInput address,
        KichenTypeEnum kitchenType,
        LocalDateTime startOperationTime,
        LocalDateTime endOperationTime,
        Long proprietaryId
) {
}
