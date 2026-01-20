package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import br.com.imsa.easyfood.infra.mappers.UserTypeMapper;
import br.com.imsa.easyfood.infra.model.UserTypeJpaEntity;
import br.com.imsa.easyfood.infra.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class UserTypeEntityRepository implements UserTypeGateway {

    private final UserTypeRepository repository;
    private final UserTypeMapper mapper;

    @Override
    public Optional<UserType> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<UserType> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<UserType> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable).map(mapper::toDomain);
    }

    @Override
    public UserType save(UserType userType) {
        UserTypeJpaEntity entity = mapper.toEntity(userType);
        UserTypeJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(UserType userType) {
        if (userType.getId() != null) {
            repository.deleteById(userType.getId());
        } else {
            UserTypeJpaEntity entity = mapper.toEntity(userType);
            repository.delete(entity);
        }
    }
}
