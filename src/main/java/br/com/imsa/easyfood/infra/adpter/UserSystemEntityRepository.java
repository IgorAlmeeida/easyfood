package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import br.com.imsa.easyfood.infra.mappers.UserSystemMapper;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSystemEntityRepository implements UserSystemGateway {

    private final UserSystemRepository repository;
    private final UserSystemMapper mapper;

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
        UserSystemJpaEntity entity = mapper.toEntity(userSystem);
        UserSystemJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(UserSystem userSystem) {
        if (userSystem.getId() != null) {
            repository.deleteById(userSystem.getId());
        } else {
            UserSystemJpaEntity entity = mapper.toEntity(userSystem);
            repository.delete(entity);
        }
    }
}
