package br.com.imsa.easyfood.domain.dto.output.restaurantitem;

public record UpdateRestaurantItemOutput(
        Long id,
        String description,
        Double price,
        String image
) {
}
