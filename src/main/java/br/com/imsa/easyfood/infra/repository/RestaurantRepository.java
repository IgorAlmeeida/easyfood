package br.com.imsa.easyfood.infra.repository;

import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantJpaEntity, Long> {
}
