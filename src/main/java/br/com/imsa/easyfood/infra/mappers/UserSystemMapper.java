package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserSystemMapper {

    @Mapping(source = "address", target = "addressJpaEntity")
    UserSystemJpaEntity toEntity(UserSystem domain, @Context PasswordEncoder passwordEncoder);

    default UserSystem toDomain(UserSystemJpaEntity entity) {
        if (entity == null) return null;
        AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
        UserTypeMapper userTypeMapper = Mappers.getMapper(UserTypeMapper.class);
        return new UserSystem(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getPassword(),
                entity.isActive(),
                userTypeMapper.toDomain(entity.getUserType()),
                addressMapper.toDomain(entity.getAddressJpaEntity())
        );
    }

    @AfterMapping
    default void encodePassword(UserSystem domain, @MappingTarget UserSystemJpaEntity entity, @Context PasswordEncoder passwordEncoder) {
        if (domain == null) return;
        String pwd = domain.getPassword();
        if (pwd == null || pwd.isBlank()) return;
        if (isBCryptHash(pwd)) {
            entity.setPassword(pwd);
        } else if (passwordEncoder != null) {
            entity.setPassword(passwordEncoder.encode(pwd));
        }
    }

    default boolean isBCryptHash(String pwd) {
        return pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$");
    }
}
