package br.com.imsa.easyfood.domain.dto.input.restaurantitem;

import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;

public record CreateRestaurantItemInput(
        String description,
        Double price,
        String image,
        AvailabilityEnum availability,
        Long restaurant
) {
}
