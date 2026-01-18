package br.com.imsa.easyfood.domain.usercase.address;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateAddressUserCase {

    private final AddressGateway addressGateway;

    public void execute (UpdateAddressInput updateAddressInput) {
        Address address = new Address();
        if (updateAddressInput.street() != null) address.setStreet(updateAddressInput.street());
        if (updateAddressInput.neighborhood() != null) address.setNeighborhood(updateAddressInput.neighborhood());
        if (updateAddressInput.city() != null) address.setCity(updateAddressInput.city());
        if (updateAddressInput.number() != null) address.setNumber(updateAddressInput.number());
        if (updateAddressInput.zipCode() != null) address.setZipCode(updateAddressInput.zipCode());

        addressGateway.update(address.getId(), address);
    }
}
