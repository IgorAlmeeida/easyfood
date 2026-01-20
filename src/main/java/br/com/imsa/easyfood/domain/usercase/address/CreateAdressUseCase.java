package br.com.imsa.easyfood.domain.usercase.address;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateAdressUseCase {

    private final AddressGateway addressGateway;

    @Transactional
    public void execute(CreateAddressInput createAddressInput) {
        Address address = new Address(
                createAddressInput.street(),
                createAddressInput.neighborhood(),
                createAddressInput.city(),
                createAddressInput.number(),
                createAddressInput.zipCode()
        );
        addressGateway.save(address);
    }


}
