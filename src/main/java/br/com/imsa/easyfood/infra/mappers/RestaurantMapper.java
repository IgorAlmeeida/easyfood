package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, UserSystemMapper.class})
public interface RestaurantMapper {

    RestaurantJpaEntity toEntity(Restaurant domain);

    Restaurant toDomain(RestaurantJpaEntity entity);
}
