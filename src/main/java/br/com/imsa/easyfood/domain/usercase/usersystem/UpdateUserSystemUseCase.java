package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.UpdateUserSystemInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class UpdateUserSystemUseCase {

    private final UserSystemGateway userSystemGateway;
    private final AddressGateway addressGateway;

    public Optional<UserSystem> execute(UpdateUserSystemInput input) {
        if (input == null || input.id() == null) return Optional.empty();

        Optional<UserSystem> opt = userSystemGateway.findById(input.id());
        if (opt.isEmpty()) return Optional.empty();

        UserSystem user = opt.get();
        if (input.username() != null) user.setUsername(input.username());
        if (input.name() != null) user.setName(input.name());
        if (input.email() != null) user.setEmail(input.email());
        if (input.userType() != null) user.setUserType(input.userType());
        if (input.active() != null) user.setActive(input.active());

        UpdateAddressInput addrInput = input.address();
        if (addrInput != null) {
            Address addr = user.getAddress();
            if (addr == null) {
                addr = new Address();
                user.setAddress(addr);
            }
            if (addrInput.street() != null) addr.setStreet(addrInput.street());
            if (addrInput.neighborhood() != null) addr.setNeighborhood(addrInput.neighborhood());
            if (addrInput.city() != null) addr.setCity(addrInput.city());
            if (addrInput.number() != null) addr.setNumber(addrInput.number());
            if (addrInput.zipCode() != null) addr.setZipCode(addrInput.zipCode());

            if (addrInput.id() != null) {
                addressGateway.update(addrInput.id(), addr);
            } else {
                Address saved = addressGateway.save(addr);
                user.setAddress(saved);
            }
        }

        UserSystem saved = userSystemGateway.save(user);
        return Optional.ofNullable(saved);
    }
}
