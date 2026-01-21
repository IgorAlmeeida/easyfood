package br.com.imsa.easyfood.domain.usercase.address;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAddressUserCaseTest {

    @Mock
    private AddressGateway addressGateway;

    @InjectMocks
    private UpdateAddressUserCase useCase;

    @Test
    @DisplayName("Deve retornar Optional.empty quando input é null ou id null")
    void nullInputOrId() {
        assertTrue(useCase.execute(null).isEmpty());
        UpdateAddressInput input = new UpdateAddressInput(null, null, null, null, null, null);
        assertTrue(useCase.execute(input).isEmpty());
    }

    @Test
    @DisplayName("Deve lançar quando endereço não encontrado")
    void addressNotFoundThrows() {
        UpdateAddressInput input = new UpdateAddressInput(1L, "Rua", "Bairro", "Cidade", "10", "00000-000");
        when(addressGateway.findById(1L)).thenReturn(Optional.empty());
        NegocioException ex = assertThrows(NegocioException.class, () -> useCase.execute(input));
        assertEquals("Endereço não encontrado.", ex.getMessage());
    }

    @Test
    @DisplayName("Happy path: deve atualizar e retornar endereço")
    void happyPath() {
        UpdateAddressInput input = new UpdateAddressInput(1L, "Nova", "NovoB", "NovaC", "20", "12345-678");
        when(addressGateway.findById(1L)).thenReturn(Optional.of(new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000")));
        Address updated = new Address(1L, "Nova", "NovoB", "NovaC", "20", "12345-678");
        when(addressGateway.update(eq(1L), any(Address.class))).thenReturn(updated);

        Optional<Address> out = useCase.execute(input);
        assertTrue(out.isPresent());
        Address a = out.get();
        assertAll(
                () -> assertEquals(1L, a.getId()),
                () -> assertEquals("Nova", a.getStreet()),
                () -> assertEquals("NovoB", a.getNeighborhood()),
                () -> assertEquals("NovaC", a.getCity()),
                () -> assertEquals("20", a.getNumber()),
                () -> assertEquals("12345-678", a.getZipCode())
        );
        verify(addressGateway).update(eq(1L), any(Address.class));
    }
}
