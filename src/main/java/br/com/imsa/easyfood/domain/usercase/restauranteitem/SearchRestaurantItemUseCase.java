package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.gateway.RestaurantItemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class SearchRestaurantItemUseCase {

    private final RestaurantItemGateway restaurantItemGateway;

    public Page<RestaurantItem> execute(Pageable pageable) {
        return restaurantItemGateway.findAll(pageable);
    }

    public Page<RestaurantItem> execute(Pageable pageable, String description) {
        if (description == null || description.isBlank()) {
            return restaurantItemGateway.findAll(pageable);
        }
        return restaurantItemGateway.findByDescriptionContainingIgnoreCase(description, pageable);
    }

    public Optional<RestaurantItem> findById(Long id) {
        return restaurantItemGateway.findById(id);
    }
}
