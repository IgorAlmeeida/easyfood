package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    default AddressJpaEntity toEntity(Address address) {
        if (address == null) return null;
        AddressJpaEntity entity = new AddressJpaEntity();
        entity.setId(address.getId());
        entity.setStreet(address.getStreet());
        entity.setNeighborhood(address.getNeighborhood());
        entity.setCity(address.getCity());
        entity.setNumber(address.getNumber());
        entity.setZipCode(address.getZipCode());
        return entity;
    }

    default Address toDomain(AddressJpaEntity entity) {
        if (entity == null) return null;
        return new Address(
                entity.getId(),
                entity.getStreet(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getNumber(),
                entity.getZipCode()
        );
    }

    default void update(@MappingTarget AddressJpaEntity target, Address request) {
        if (target == null || request == null) return;
        target.setStreet(request.getStreet());
        target.setNeighborhood(request.getNeighborhood());
        target.setCity(request.getCity());
        target.setNumber(request.getNumber());
        target.setZipCode(request.getZipCode());
    }
}
