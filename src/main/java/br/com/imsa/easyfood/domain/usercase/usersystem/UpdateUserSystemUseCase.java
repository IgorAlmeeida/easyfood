package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.UpdateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.output.address.UpdateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.UpdateUserSystemOutput;
import br.com.imsa.easyfood.domain.dto.output.usertype.UpdateUserTypeOutput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class UpdateUserSystemUseCase {

    private final UserSystemGateway userSystemGateway;
    private final AddressGateway addressGateway;
    private final UserTypeGateway userTypeGateway;

    public Optional<UpdateUserSystemOutput> execute(UpdateUserSystemInput input) {
        if (input == null || input.id() == null) {
            return Optional.empty();
        }

        Optional<UserSystem> opt = userSystemGateway.findById(input.id());
        if (opt.isEmpty()){
            return Optional.empty();
        }

        UserSystem current = opt.get();

        // Merge address
        UpdateAddressInput addrInput = input.address();
        Address address = new Address(addrInput.id(), addrInput.street(), addrInput.neighborhood(), addrInput.city(), addrInput.number(), addrInput.zipCode() );

        UserType userType = userTypeGateway.findById(input.userType())
                .orElseThrow(() -> new NegocioException("Tipo de usuário não encontrado."));

        // Rebuild UserSystem with merged fields
        UserSystem mergedUser = new UserSystem(
                current.getId(),
                input.name(),
                input.email(),
                input.username(),
                current.getPassword(),
                input.active(),
                userType,
                address
        );

        UserSystem saved = userSystemGateway.save(mergedUser);

        if (saved == null) {
            return Optional.empty();
        }

        UpdateAddressOutput addressOutput = null;

        if (saved.getAddress() != null) {
            Address a = saved.getAddress();
            addressOutput = new UpdateAddressOutput(
                    a.getId(),
                    a.getStreet(),
                    a.getNeighborhood(),
                    a.getCity(),
                    a.getNumber(),
                    a.getZipCode()
            );
        }

        UpdateUserTypeOutput  userTypeOutput = new UpdateUserTypeOutput(
                userType.getId(),
                userType.getName()
        );

        UpdateUserSystemOutput output = new UpdateUserSystemOutput(
                saved.getId(),
                saved.getUsername(),
                saved.getName(),
                saved.getEmail(),
                userTypeOutput,
                saved.isActive(),
                addressOutput
        );
        return Optional.of(output);
    }
}
