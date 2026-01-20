package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.CreateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.CreateUserSystemOutput;
import br.com.imsa.easyfood.domain.dto.output.usertype.CreateUserTypeOutput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateUserSystemUseCase {

    private final UserSystemGateway userSystemGateway;
    private final AddressGateway addressGateway;
    private final UserTypeGateway userTypeGateway;

    @Transactional
    public CreateUserSystemOutput execute(CreateUserSystemInput input) {
        Address addr = null;
        if (input.address() != null) {
            addr = new Address(
                    input.address().street(),
                    input.address().neighborhood(),
                    input.address().city(),
                    input.address().number(),
                    input.address().zipCode()
            );
        }

        UserType userType = userTypeGateway.findById(input.userType())
                .orElseThrow(() -> new NegocioException("Tipo de usuário não encontrado."));

        UserSystem user = new UserSystem(
                null,
                input.name(),
                input.email(),
                input.username(),
                input.password(),
                true,
                userType,
                addr
        );

        UserSystem savedUser = userSystemGateway.save(user);

        Address a = savedUser.getAddress();
        CreateAddressOutput addressOutput = new CreateAddressOutput(
                a.getId(),
                a.getStreet(),
                a.getNeighborhood(),
                a.getCity(),
                a.getNumber(),
                a.getZipCode()
        );

        CreateUserTypeOutput userTypeOutput = new CreateUserTypeOutput(
                savedUser.getUserType().getId(),
                savedUser.getUserType().getName()
        );

        return new CreateUserSystemOutput(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getName(),
                savedUser.getEmail(),
                userTypeOutput,
                savedUser.isActive(),
                addressOutput
        );
    }
}
