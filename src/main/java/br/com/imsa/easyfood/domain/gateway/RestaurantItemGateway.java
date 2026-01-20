package br.com.imsa.easyfood.domain.gateway;

import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantItemGateway {

    Optional<RestaurantItem> findById(Long id);

    Page<RestaurantItem> findAll(Pageable pageable);

    Page<RestaurantItem> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    Page<RestaurantItem> findByRestaurantId(Long restaurantId, Pageable pageable);

    Page<RestaurantItem> findByRestaurantIdAndDescriptionContainingIgnoreCase(Long restaurantId, String description, Pageable pageable);

    RestaurantItem save(RestaurantItem item);

    void delete(RestaurantItem item);
}
