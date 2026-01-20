package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSystemMapper {

    private final AddressMapper addressMapper;
    private final UserTypeMapper userTypeMapper;

    public UserSystemMapper(AddressMapper addressMapper, UserTypeMapper userTypeMapper) {
        this.addressMapper = addressMapper;
        this.userTypeMapper = userTypeMapper;
    }

    public UserSystemJpaEntity toEntity(UserSystem domain, PasswordEncoder passwordEncoder) {
        if (domain == null) return null;
        UserSystemJpaEntity entity = new UserSystemJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setUsername(domain.getUsername());
        entity.setActive(domain.isActive());
        // user type
        entity.setUserType(userTypeMapper.toEntity(domain.getUserType()));
        // address
        AddressJpaEntity addressEntity = addressMapper.toEntity(domain.getAddress());
        entity.setAddressJpaEntity(addressEntity);
        // password encoding logic
        String pwd = domain.getPassword();
        if (pwd != null && !pwd.isBlank()) {
            if (isBCryptHash(pwd)) {
                entity.setPassword(pwd);
            } else if (passwordEncoder != null) {
                entity.setPassword(passwordEncoder.encode(pwd));
            }
        }
        return entity;
    }

    public UserSystem toDomain(UserSystemJpaEntity entity) {
        if (entity == null) return null;
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

    public boolean isBCryptHash(String pwd) {
        return pwd != null && (pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$"));
    }
}
