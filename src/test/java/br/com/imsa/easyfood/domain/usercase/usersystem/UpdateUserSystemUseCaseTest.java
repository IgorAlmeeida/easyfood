package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.UpdateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.UpdateUserSystemOutput;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserSystemUseCaseTest {

    @Mock private UserSystemGateway userSystemGateway;
    @Mock private AddressGateway addressGateway;
    @Mock private UserTypeGateway userTypeGateway;

    @InjectMocks private UpdateUserSystemUseCase useCase;

    private UserSystem currentUser() {
        Address addr = new Address(10L, "Rua", "Bairro", "Cidade", "100", "00000-000");
        UserType type = new UserType(1L, "ADMIN");
        return new UserSystem(5L, "Nome", "a@b.com", "user", "secret1", true, type, addr);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando input é null ou id null")
    void nullInputs() {
        assertTrue(useCase.execute(null).isEmpty());
        assertTrue(useCase.execute(new UpdateUserSystemInput(null, "user","Nome","a@b.com", 1L, true, new UpdateAddressInput(1L,"r","b","c","n","z"))).isEmpty());
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando usuário não encontrado")
    void userNotFound() {
        when(userSystemGateway.findById(5L)).thenReturn(Optional.empty());
        UpdateUserSystemInput in = new UpdateUserSystemInput(5L, "user","Nome","a@b.com", 1L, true, new UpdateAddressInput(1L,"r","b","c","n","z"));
        assertTrue(useCase.execute(in).isEmpty());
    }

    @Test
    @DisplayName("Deve lançar quando userType não encontrado")
    void userTypeNotFound() {
        when(userSystemGateway.findById(5L)).thenReturn(Optional.of(currentUser()));
        when(userTypeGateway.findById(2L)).thenReturn(Optional.empty());
        UpdateUserSystemInput in = new UpdateUserSystemInput(5L, "user","Novo","novo@a.com", 2L, false, new UpdateAddressInput(10L,"Rua2","Bairro2","Cidade2","200","11111-111"));
        NegocioException e = assertThrows(NegocioException.class, () -> useCase.execute(in));
        assertEquals("Tipo de usuário não encontrado.", e.getMessage());
    }

    @Test
    @DisplayName("Happy path: deve atualizar usuário e retornar output")
    void happyPath() {
        UserSystem current = currentUser();
        when(userSystemGateway.findById(5L)).thenReturn(Optional.of(current));
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(current.getUserType()));
        when(userSystemGateway.save(any(UserSystem.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateAddressInput addr = new UpdateAddressInput(10L, "RuaNova", "BairroN", "CidadeN", "200", "11111-111");
        UpdateUserSystemInput in = new UpdateUserSystemInput(5L, "user","NomeNovo","novo@a.com", 1L, false, addr);

        Optional<UpdateUserSystemOutput> out = useCase.execute(in);
        assertTrue(out.isPresent());
        UpdateUserSystemOutput o = out.get();
        assertAll(
                () -> assertEquals(5L, o.id()),
                () -> assertEquals("user", o.username()),
                () -> assertEquals("NomeNovo", o.name()),
                () -> assertEquals("novo@a.com", o.email()),
                () -> assertFalse(o.active()),
                () -> assertNotNull(o.address()),
                () -> assertEquals("RuaNova", o.address().street())
        );
    }
}
