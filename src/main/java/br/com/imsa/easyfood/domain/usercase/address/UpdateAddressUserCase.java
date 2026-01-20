package br.com.imsa.easyfood.domain.usercase.address;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class UpdateAddressUserCase {

    private final AddressGateway addressGateway;

    public Optional<Address> execute(UpdateAddressInput updateAddressInput) {
        if (updateAddressInput == null || updateAddressInput.id() == null) {
            return Optional.empty();
        }

        Optional<Address> currentOpt = addressGateway.findById(updateAddressInput.id());
        if (currentOpt.isEmpty()){
            throw new NegocioException("Endereço não encontrado.");
        }

        Address merged = new Address(updateAddressInput.street(), updateAddressInput.neighborhood(),
                updateAddressInput.city(), updateAddressInput.number(), updateAddressInput.zipCode() );
        Address updated = addressGateway.update(updateAddressInput.id(), merged);

        return Optional.ofNullable(updated);
    }
}
