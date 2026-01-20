package br.com.imsa.easyfood.domain.dto.input.restaurantitem;

public record CreateRestaurantItemInput(
        String description,
        Double price,
        String image
) {
}
