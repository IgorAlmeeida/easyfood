package br.com.imsa.easyfood.infra.repository;

import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressJpaEntity, Long> {
}
