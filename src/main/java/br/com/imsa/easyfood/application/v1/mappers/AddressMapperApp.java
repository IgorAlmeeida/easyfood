package br.com.imsa.easyfood.application.v1.mappers;

import br.com.imsa.easyfood.application.v1.dto.requests.AddressRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.AddressResponse;
import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.domain.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapperApp {

    AddressResponse toResponse(CreateAddressOutput address);

    // Map domain Address directly to API response
    AddressResponse toResponse(Address address);

    CreateAddressInput toCreateAddressInput(AddressRequest address);

    UpdateAddressInput toUpdateAddressInput(AddressRequest address);
}
