package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public abstract class UserSystemMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(source = "address", target = "addressJpaEntity")
    public abstract UserSystemJpaEntity toEntity(UserSystem domain);

    @Mapping(source = "addressJpaEntity", target = "address")
    public abstract UserSystem toDomain(UserSystemJpaEntity entity);

    @AfterMapping
    protected void encodePassword(UserSystem domain, @MappingTarget UserSystemJpaEntity entity) {
        if (domain == null) return;
        String pwd = domain.getPassword();
        if (pwd == null || pwd.isBlank()) return;
        if (isBCryptHash(pwd)) {
            entity.setPassword(pwd);
        } else if (passwordEncoder != null) {
            entity.setPassword(passwordEncoder.encode(pwd));
        }
    }

    private boolean isBCryptHash(String pwd) {
        return pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$");
    }
}
