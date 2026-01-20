package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.dto.input.restaurantitem.CreateRestaurantItemInput;
import br.com.imsa.easyfood.domain.dto.output.restaurantitem.CreateRestaurantItemOutput;
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
public class CreateRestaurantItemUseCase {

    private final RestaurantItemGateway restaurantItemGateway;
    private final RestaurantGateway restaurantGateway;

    public Optional<CreateRestaurantItemOutput> execute(CreateRestaurantItemInput input) {
        if (input == null){
            return Optional.empty();
        }

        Restaurant restaurant = restaurantGateway.findById(input.restaurant())
                .orElseThrow(() -> new NegocioException("Restaurante não encontrado no sistema."));

        RestaurantItem item = new RestaurantItem(
                input.description(),
                input.price(),
                input.image(),
                input.availability(),
                restaurant
        );
        RestaurantItem saved = restaurantItemGateway.save(item);
        if (saved == null){
            return Optional.empty();
        }
        return Optional.of(new CreateRestaurantItemOutput(saved.getId(), saved.getDescription(), saved.getPrice(), saved.getImage()));
    }
}
