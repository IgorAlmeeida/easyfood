package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.CreateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.CreateUserSystemOutput;
import br.com.imsa.easyfood.domain.dto.output.usertype.CreateUserTypeOutput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserSystemUseCaseTest {

    @Mock private UserSystemGateway userSystemGateway;
    @Mock private AddressGateway addressGateway;
    @Mock private UserTypeGateway userTypeGateway;

    @InjectMocks private CreateUserSystemUseCase useCase;

    private CreateUserSystemInput input() {
        CreateAddressInput addr = new CreateAddressInput("Rua","Bairro","Cidade","10","00000-000");
        return new CreateUserSystemInput("user","Nome","a@b.com", 1L, addr, "secret1");
    }

    @Test
    @DisplayName("Happy path: deve criar usuário e retornar output populado")
    void happyPath() {
        when(userTypeGateway.findById(1L)).thenReturn(java.util.Optional.of(new UserType(1L, "ADMIN")));
        when(userSystemGateway.save(any(UserSystem.class))).thenAnswer(inv -> {
            UserSystem u = inv.getArgument(0);
            Address a = new Address(10L, u.getAddress().getStreet(), u.getAddress().getNeighborhood(), u.getAddress().getCity(), u.getAddress().getNumber(), u.getAddress().getZipCode());
            return new UserSystem(99L, u.getName(), u.getEmail(), u.getUsername(), u.getPassword(), u.isActive(), u.getUserType(), a);
        });

        CreateUserSystemOutput out = useCase.execute(input());
        assertAll(
                () -> assertEquals(99L, out.id()),
                () -> assertEquals("user", out.username()),
                () -> assertEquals("Nome", out.name()),
                () -> assertEquals("a@b.com", out.email()),
                () -> assertTrue(out.active()),
                () -> assertNotNull(out.address()),
                () -> assertEquals("ADMIN", out.userType().name())
        );
    }

    @Test
    @DisplayName("Deve lançar quando userType não encontrado")
    void userTypeNotFound() {
        when(userTypeGateway.findById(1L)).thenReturn(java.util.Optional.empty());
        NegocioException e = assertThrows(NegocioException.class, () -> useCase.execute(input()));
        assertEquals("Tipo de usuário não encontrado.", e.getMessage());
    }

    @Test
    @DisplayName("Deve lançar NegocioException quando address do input for null (invariante do domínio)")
    void nullAddressLeadsToDomainException() {
        CreateUserSystemInput i = new CreateUserSystemInput("user","Nome","a@b.com", 1L, null, "secret1");
        when(userTypeGateway.findById(1L)).thenReturn(java.util.Optional.of(new UserType(1L, "ADMIN")));
        NegocioException e = assertThrows(NegocioException.class, () -> useCase.execute(i));
        assertEquals("O endereço é obrigatório.", e.getMessage());
    }
}
