package br.com.imsa.easyfood.infra.repository;

import br.com.imsa.easyfood.infra.model.RestaurantItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantItemRepository extends JpaRepository<RestaurantItemJpaEntity, Long> {
}
