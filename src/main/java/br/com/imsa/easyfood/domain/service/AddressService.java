package br.com.imsa.easyfood.domain.service;

import br.com.imsa.easyfood.api.dto.requests.AddressRequest;
import br.com.imsa.easyfood.domain.entity.Address;

public interface AddressService {

    Address createAddress(AddressRequest addressRequest);

    Address updateAddress(Address address, AddressRequest addressRequest);
}
