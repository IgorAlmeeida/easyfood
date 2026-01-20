package br.com.imsa.easyfood.domain.dto.output.restaurantitem;

public record CreateRestaurantItemOutput(
        Long id,
        String description,
        Double price,
        String image
) {
}
