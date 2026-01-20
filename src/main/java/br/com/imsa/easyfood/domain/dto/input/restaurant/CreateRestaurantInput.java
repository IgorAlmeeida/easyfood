package br.com.imsa.easyfood.domain.dto.input.restaurant;

import java.time.LocalDateTime;

public record CreateRestaurantInput(
        String name,
        Long addressId,
        String kitchenType,
        LocalDateTime startOperationTime,
        LocalDateTime endOperationTime,
        Long proprietaryId
) {
}
