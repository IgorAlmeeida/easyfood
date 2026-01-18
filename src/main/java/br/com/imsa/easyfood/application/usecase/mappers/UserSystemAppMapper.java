package br.com.imsa.easyfood.application.usecase.mappers;

import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemUpdateRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.UserSystemResponse;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserSystemAppMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "address", ignore = true)
    UserSystem toDomain(UserSystemCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "address", ignore = true)
    void update(@MappingTarget UserSystem target, UserSystemUpdateRequest source);

    UserSystemResponse toResponse(UserSystem domain);
}
