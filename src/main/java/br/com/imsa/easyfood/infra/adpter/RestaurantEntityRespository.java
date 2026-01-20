package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import br.com.imsa.easyfood.infra.mappers.RestaurantMapper;
import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import br.com.imsa.easyfood.infra.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantEntityRespository implements RestaurantGateway {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    @Override
    public Optional<Restaurant> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Restaurant> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable).map(mapper::toDomain);
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantJpaEntity entity = mapper.toEntity(restaurant);
        RestaurantJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(Restaurant restaurant) {
        repository.deleteById(restaurant.getId());
    }
}
