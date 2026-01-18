package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemUpdateRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.UserSystemResponse;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserSystemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "address", ignore = true) // handled outside or via AddressService
    UserSystemJpaEntity toEntity(UserSystemCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "address", ignore = true) // Address handled by AddressService
    void update(@MappingTarget UserSystemJpaEntity target, UserSystemUpdateRequest source);

    @Mapping(target = "createAt", source = "createAt")
    @Mapping(target = "updateAt", source = "updateAt")
    UserSystemResponse toResponse(UserSystemJpaEntity entity);
}
