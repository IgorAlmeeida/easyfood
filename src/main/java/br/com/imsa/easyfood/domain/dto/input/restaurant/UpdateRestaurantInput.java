package br.com.imsa.easyfood.domain.dto.input.restaurant;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;

import java.time.LocalDateTime;

public record UpdateRestaurantInput(
        Long id,
        String name,
        UpdateAddressInput address,
        KichenTypeEnum kitchenType,
        LocalDateTime startOperationTime,
        LocalDateTime endOperationTime,
        Long proprietaryId
) {
}
