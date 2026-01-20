package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class SearchRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;

    public Page<Restaurant> execute(Pageable pageable) {
        return restaurantGateway.findAll(pageable);
    }

    public Page<Restaurant> execute(Pageable pageable, String name) {
        if (name == null || name.isBlank()) {
            return restaurantGateway.findAll(pageable);
        }
        return restaurantGateway.findByNameContainingIgnoreCase(name, pageable);
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurantGateway.findById(id);
    }
}
