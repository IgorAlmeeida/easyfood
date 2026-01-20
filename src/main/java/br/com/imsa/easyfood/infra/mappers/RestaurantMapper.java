package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    private final AddressMapper addressMapper;
    private final UserSystemMapper userSystemMapper;

    public RestaurantMapper(AddressMapper addressMapper, UserSystemMapper userSystemMapper) {
        this.addressMapper = addressMapper;
        this.userSystemMapper = userSystemMapper;
    }

    public RestaurantJpaEntity toEntity(Restaurant domain){
        if (domain == null) return null;

        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setAddress(domain.getAddress() != null ? addressMapper.toEntity(domain.getAddress()) : null);
        entity.setKitchenType(domain.getKitchenType());
        entity.setStartOperationTime(domain.getStartOperationTime());
        entity.setEndOperationTime(domain.getEndOperationTime());
        entity.setProprietary(domain.getProprietary() != null ? userSystemMapper.toEntity(domain.getProprietary(), null) : null);
        return entity;
    }

    public Restaurant toDomain(RestaurantJpaEntity entity) {
        if (entity == null) return null;

        UserSystem proprietary = userSystemMapper.toDomain(entity.getProprietary());

        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getAddress() != null ? addressMapper.toDomain(entity.getAddress()) : null,
                entity.getKitchenType(),
                entity.getStartOperationTime(),
                entity.getEndOperationTime(),
                entity.getProprietary() != null ? proprietary : null
        );
    }
}
