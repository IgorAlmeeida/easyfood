package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.infra.model.RestaurantItemJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantItemMapper {

    RestaurantItemJpaEntity toEntity(RestaurantItem domain);

    RestaurantItem toDomain(RestaurantItemJpaEntity entity);
}
