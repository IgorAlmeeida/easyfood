package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.CreateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.CreateUserSystemOutput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateUserSystemUseCase {

    private final UserSystemGateway userSystemGateway;
    private final AddressGateway addressGateway;

    @Transactional
    public CreateUserSystemOutput execute(CreateUserSystemInput input) {
        // Create and persist Address if provided
        Address savedAddress = null;

        UserSystem user = new UserSystem();
        user.setUsername(input.username());
        user.setName(input.name());
        user.setEmail(input.email());
        user.setUserType(input.userType());
        user.setActive(true);
        user.setPassword(input.password());

        CreateAddressInput addrInput = input.address();
        Address addr = new Address();
        addr.setStreet(addrInput.street());
        addr.setNeighborhood(addrInput.neighborhood());
        addr.setCity(addrInput.city());
        addr.setNumber(addrInput.number());
        addr.setZipCode(addrInput.zipCode());

        user.setAddress(addr);


        UserSystem savedUser = userSystemGateway.save(user);

        CreateAddressOutput addressOutput = null;
        if (savedUser.getAddress() != null) {
            Address a = savedUser.getAddress();
            addressOutput = new CreateAddressOutput(
                    a.getId(),
                    a.getStreet(),
                    a.getNeighborhood(),
                    a.getCity(),
                    a.getNumber(),
                    a.getZipCode(),
                    a.getCreateAt(),
                    a.getUpdateAt()
            );
        }

        return new CreateUserSystemOutput(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getUserType(),
                savedUser.isActive(),
                addressOutput
        );
    }
}
