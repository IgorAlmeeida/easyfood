package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.infra.model.RestaurantItemJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RestaurantItemMapper {

    private final RestaurantMapper restaurantMapper;

    public RestaurantItemMapper(RestaurantMapper restaurantMapper) {
        this.restaurantMapper = restaurantMapper;
    }

    public RestaurantItemJpaEntity toEntity(RestaurantItem domain) {
        if (domain == null) return null;
        RestaurantItemJpaEntity entity = new RestaurantItemJpaEntity();
        entity.setId(domain.getId());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        entity.setImage(domain.getImage());
        entity.setAvailability(domain.getAvailability());
        // map restaurant with all relevant fields using RestaurantMapper
        if (domain.getRestaurant() != null) {
            entity.setRestaurant(restaurantMapper.toEntity(domain.getRestaurant()));
        }
        return entity;
    }

    public RestaurantItem toDomain(RestaurantItemJpaEntity entity) {
        if (entity == null) return null;
        // map restaurant with all relevant fields using RestaurantMapper
        Restaurant restaurant = null;
        if (entity.getRestaurant() != null) {
            restaurant = restaurantMapper.toDomain(entity.getRestaurant());
        }
        return new RestaurantItem(
                entity.getId(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImage(),
                entity.getAvailability(),
                restaurant
        );
    }
}
