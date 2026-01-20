package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.infra.model.UserTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserTypeMapper {

    public UserTypeJpaEntity toEntity(UserType domain) {
        if (domain == null) return null;
        UserTypeJpaEntity entity = new UserTypeJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        return entity;
    }

    public UserType toDomain(UserTypeJpaEntity entity) {
        if (entity == null) return null;
        return new UserType(entity.getId(), entity.getName());
    }
}
