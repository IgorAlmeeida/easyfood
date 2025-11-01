package br.com.imsa.easyfood.infrastructure.repository;

import br.com.imsa.easyfood.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
