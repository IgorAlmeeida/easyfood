package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import br.com.imsa.easyfood.infra.mappers.UserSystemMapper;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import br.com.imsa.easyfood.infra.model.UserTypeJpaEntity;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
import br.com.imsa.easyfood.infra.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSystemEntityRepository implements UserSystemGateway {

    private final UserSystemRepository repository;
    private final UserTypeRepository userTypeRepository;
    private final UserSystemMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserSystem> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<UserSystem> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<UserSystem> findByEmail(String email) {
        return repository.findUserSystemByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Page<UserSystem> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<UserSystem> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findUserSystemByNameContainingIgnoreCase(name, pageable).map(mapper::toDomain);
    }

    @Override
    public UserSystem save(UserSystem userSystem) {
        UserSystemJpaEntity entity = mapper.toEntity(userSystem, passwordEncoder);
        UserTypeJpaEntity userTypeJpaEntity = userTypeRepository.findById(entity.getUserType().getId())
                .orElse(null);
        entity.setUserType(userTypeJpaEntity);
        UserSystemJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(UserSystem userSystem) {
        repository.deleteById(userSystem.getId());
    }
}
