package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.api.dto.requests.AddressRequest;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.service.AddressService;
import br.com.imsa.easyfood.infrastructure.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    public Address createAddress(AddressRequest addressRequest) {
        Address address = modelMapper.map(addressRequest, Address.class);
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Address address, AddressRequest addressRequest) {
        // Map incoming request fields onto the existing entity instance
        modelMapper.map(addressRequest, address);
        return addressRepository.save(address);
    }
}
