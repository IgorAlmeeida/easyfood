package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.dto.input.restaurantitem.UpdateRestaurantItemInput;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.gateway.RestaurantItemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class UpdateRestaurantItemUseCase {

    private final RestaurantItemGateway restaurantItemGateway;

    public Optional<RestaurantItem> execute(UpdateRestaurantItemInput input) {
        if (input == null || input.id() == null) return Optional.empty();
        Optional<RestaurantItem> opt = restaurantItemGateway.findById(input.id());
        if (opt.isEmpty()) return Optional.empty();
        RestaurantItem item = opt.get();
        if (input.description() != null) item.setDescription(input.description());
        if (input.price() != null) item.setPrice(input.price());
        if (input.image() != null) item.setImage(input.image());
        RestaurantItem saved = restaurantItemGateway.save(item);
        return Optional.ofNullable(saved);
    }
}
