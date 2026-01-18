package br.com.imsa.easyfood.domain.gateway;

import br.com.imsa.easyfood.domain.entity.Address;

public interface AddressGateway {

    Address save(Address address);

    Address update(Long id, Address address);

}
