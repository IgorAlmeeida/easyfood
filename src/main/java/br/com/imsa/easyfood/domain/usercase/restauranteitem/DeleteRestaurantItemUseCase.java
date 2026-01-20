package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.gateway.RestaurantItemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteRestaurantItemUseCase {

    private final RestaurantItemGateway restaurantItemGateway;

    public boolean execute(Long id) {
        return restaurantItemGateway.findById(id)
                .map(item -> { restaurantItemGateway.delete(item); return true; })
                .orElse(false);
    }
}
