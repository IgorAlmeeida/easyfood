package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.dto.input.restaurantitem.UpdateRestaurantItemInput;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import br.com.imsa.easyfood.domain.gateway.RestaurantItemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class UpdateRestaurantItemUseCase {

    private final RestaurantItemGateway restaurantItemGateway;
    private final RestaurantGateway  restaurantGateway;

    public Optional<RestaurantItem> execute(UpdateRestaurantItemInput input) {
        if (input == null || input.id() == null){
            return Optional.empty();
        }

        Optional<RestaurantItem> opt = restaurantItemGateway.findById(input.id());
        if (opt.isEmpty()){
            return Optional.empty();
        }

        RestaurantItem current = opt.get();

        Restaurant restaurant = restaurantGateway.findById(input.restaurant())
                .orElseThrow(() -> new NegocioException("Restaurante não encontrado no sistema."));

        RestaurantItem merged = new RestaurantItem(current.getId(), input.description(), input.price(), input.image(),  input.availability(), restaurant);
        RestaurantItem saved = restaurantItemGateway.save(merged);

        return Optional.ofNullable(saved);
    }
}
