package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressJpaEntity toEntity(Address address);

    Address toDomain(AddressJpaEntity entity);

    void update(@MappingTarget AddressJpaEntity target, Address request);

}
