package br.com.imsa.easyfood.domain.gateway;

import br.com.imsa.easyfood.domain.entity.Address;

import java.util.Optional;

public interface AddressGateway {

    Optional<Address> findById(Long id);

    Address save(Address address);

    Address update(Long id, Address address);

}
