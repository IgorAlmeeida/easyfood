package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;

    public boolean execute(Long id) {
        return restaurantGateway.findById(id)
                .map(r -> { restaurantGateway.delete(r); return true; })
                .orElse(false);
    }
}
