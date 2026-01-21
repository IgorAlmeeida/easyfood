package br.com.imsa.easyfood.domain.usercase.address;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateAdressUseCaseTest {

    @Mock
    private AddressGateway addressGateway;

    @InjectMocks
    private CreateAdressUseCase useCase;

    @Test
    @DisplayName("Happy path: deve criar Address a partir do input e chamar save")
    void happyPath() {
        CreateAddressInput in = new CreateAddressInput("Rua", "Bairro", "Cidade", "10", "00000-000");
        useCase.execute(in);
        verify(addressGateway).save(any(Address.class));
    }

    @Test
    @DisplayName("Deve lançar NegocioException quando algum campo inválido no input")
    void invalidFieldsThrow() {
        CreateAddressInput in = new CreateAddressInput("", "", "Cidade", "10", "00000-000");
        NegocioException ex = assertThrows(NegocioException.class, () -> useCase.execute(in));
        assertEquals("O campo rua é obrigatório.", ex.getMessage());
    }
}
