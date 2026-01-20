package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.dto.input.restaurantitem.CreateRestaurantItemInput;
import br.com.imsa.easyfood.domain.dto.output.restaurantitem.CreateRestaurantItemOutput;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.gateway.RestaurantItemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class CreateRestaurantItemUseCase {

    private final RestaurantItemGateway restaurantItemGateway;

    public Optional<CreateRestaurantItemOutput> execute(CreateRestaurantItemInput input) {
        if (input == null) return Optional.empty();
        RestaurantItem item = new RestaurantItem();
        item.setDescription(input.description());
        item.setPrice(input.price());
        item.setImage(input.image());
        RestaurantItem saved = restaurantItemGateway.save(item);
        if (saved == null) return Optional.empty();
        return Optional.of(new CreateRestaurantItemOutput(saved.getId(), saved.getDescription(), saved.getPrice(), saved.getImage()));
    }
}
