package br.com.imsa.easyfood.domain.dto.input.restaurantitem;

public record UpdateRestaurantItemInput(
        Long id,
        String description,
        Double price,
        String image
) {
}
