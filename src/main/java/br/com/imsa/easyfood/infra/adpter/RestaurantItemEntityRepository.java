package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.gateway.RestaurantItemGateway;
import br.com.imsa.easyfood.infra.mappers.RestaurantItemMapper;
import br.com.imsa.easyfood.infra.model.RestaurantItemJpaEntity;
import br.com.imsa.easyfood.infra.repository.RestaurantItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantItemEntityRepository implements RestaurantItemGateway {

    private final RestaurantItemRepository repository;
    private final RestaurantItemMapper mapper;

    @Override
    public Optional<RestaurantItem> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<RestaurantItem> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<RestaurantItem> findByDescriptionContainingIgnoreCase(String description, Pageable pageable) {
        return repository.findByDescriptionContainingIgnoreCase(description, pageable).map(mapper::toDomain);
    }

    @Override
    public RestaurantItem save(RestaurantItem item) {
        RestaurantItemJpaEntity entity = mapper.toEntity(item);
        RestaurantItemJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(RestaurantItem item) {
        repository.deleteById(item.getId());
    }
}
