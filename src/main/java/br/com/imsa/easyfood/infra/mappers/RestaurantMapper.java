package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, UserSystemMapper.class})
public interface RestaurantMapper {

    RestaurantJpaEntity toEntity(Restaurant domain);

    default Restaurant toDomain(RestaurantJpaEntity entity) {
        if (entity == null) return null;
        AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
        UserSystemMapper userSystemMapper = Mappers.getMapper(UserSystemMapper.class);
        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getAddress() != null ? addressMapper.toDomain(entity.getAddress()) : null,
                entity.getKitchenType(),
                entity.getStartOperationTime(),
                entity.getEndOperationTime(),
                entity.getProprietary() != null ? userSystemMapper.toDomain(entity.getProprietary()) : null
        );
    }
}
