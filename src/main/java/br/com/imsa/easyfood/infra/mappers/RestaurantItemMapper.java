package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.infra.model.RestaurantItemJpaEntity;
import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RestaurantItemMapper {

    default RestaurantItemJpaEntity toEntity(RestaurantItem domain) {
        if (domain == null) return null;
        RestaurantItemJpaEntity entity = new RestaurantItemJpaEntity();
        entity.setId(domain.getId());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        entity.setImage(domain.getImage());
        entity.setAvailability(domain.getAvailability());
        // map restaurant (only id is required when creating)
        if (domain.getRestaurant() != null) {
            RestaurantJpaEntity r = new RestaurantJpaEntity();
            r.setId(domain.getRestaurant().getId());
            entity.setRestaurant(r);
        }
        return entity;
    }

    default RestaurantItem toDomain(RestaurantItemJpaEntity entity) {
        if (entity == null) return null;
        // map restaurant with only id to avoid deep mapping and extra dependencies
        Restaurant restaurant = null;
        if (entity.getRestaurant() != null) {
            restaurant = new Restaurant(
                    entity.getRestaurant().getId(),
                    null, null, null, null, null, null
            );
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
