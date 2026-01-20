package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.infra.model.UserTypeJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTypeMapper {

    UserTypeJpaEntity toEntity(UserType domain);

    default UserType toDomain(UserTypeJpaEntity entity) {
        if (entity == null) return null;
        return new UserType(entity.getId(), entity.getName());
    }
}
