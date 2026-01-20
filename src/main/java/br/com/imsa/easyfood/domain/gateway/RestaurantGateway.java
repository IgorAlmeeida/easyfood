package br.com.imsa.easyfood.domain.gateway;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantGateway {

    Optional<Restaurant> findById(Long id);

    Page<Restaurant> findAll(Pageable pageable);

    Page<Restaurant> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Restaurant save(Restaurant restaurant);

    void delete(Restaurant restaurant);
}
