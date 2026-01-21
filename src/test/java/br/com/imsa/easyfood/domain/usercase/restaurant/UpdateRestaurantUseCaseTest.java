package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.restaurant.UpdateRestaurantInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;
    @Mock
    private AddressGateway addressGateway;
    @Mock
    private UserSystemGateway userSystemGateway;

    @InjectMocks
    private UpdateRestaurantUseCase useCase;

    private Restaurant existingRestaurant() {
        Address addr = new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000");
        UserSystem prop = new UserSystem(1L, "Nome", "a@b.com", "user", "secret1", true, new UserType(1L, "ADMIN"), addr);
        return new Restaurant(10L, "Rest", addr, KichenTypeEnum.ITALIAN,
                LocalDateTime.of(2026,1,1,9,0), LocalDateTime.of(2026,1,1,18,0), prop);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando input é null ou id null")
    void nullInputOrId() {
        assertTrue(useCase.execute(null).isEmpty());
        UpdateRestaurantInput input = new UpdateRestaurantInput(null, null, null, null, null, null, null);
        assertTrue(useCase.execute(input).isEmpty());
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando restaurante não encontrado")
    void restaurantNotFound() {
        UpdateRestaurantInput input = new UpdateRestaurantInput(10L, null, null, null, null, null, null);
        when(restaurantGateway.findById(10L)).thenReturn(Optional.empty());
        assertTrue(useCase.execute(input).isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar endereço quando address presente no input")
    void updateAddressWhenPresent() {
        Restaurant current = existingRestaurant();
        when(restaurantGateway.findById(10L)).thenReturn(Optional.of(current));
        when(userSystemGateway.findById(1L)).thenReturn(Optional.of(current.getProprietary()));
        when(restaurantGateway.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateAddressInput addr = new UpdateAddressInput(1L, "Nova", "B", "C", "100", "99999-999");
        UpdateRestaurantInput input = new UpdateRestaurantInput(10L, "Novo Nome", addr, KichenTypeEnum.BRAZILIAN,
                current.getStartOperationTime(), current.getEndOperationTime(), 1L);

        useCase.execute(input);
        verify(addressGateway).update(eq(1L), any(Address.class));
    }

    @Test
    @DisplayName("Deve lançar quando proprietário não encontrado")
    void proprietaryNotFoundThrows() {
        Restaurant current = existingRestaurant();
        when(restaurantGateway.findById(10L)).thenReturn(Optional.of(current));
        when(userSystemGateway.findById(2L)).thenReturn(Optional.empty());

        UpdateRestaurantInput input = new UpdateRestaurantInput(10L, "Nome", null, KichenTypeEnum.ITALIAN,
                current.getStartOperationTime(), current.getEndOperationTime(), 2L);

        NegocioException ex = assertThrows(NegocioException.class, () -> useCase.execute(input));
        assertEquals("Usuário não cadastrado no sistema", ex.getMessage());
    }

    @Test
    @DisplayName("Happy path: deve salvar e retornar restaurante atualizado")
    void happyPath() {
        Restaurant current = existingRestaurant();
        when(restaurantGateway.findById(10L)).thenReturn(Optional.of(current));
        when(userSystemGateway.findById(1L)).thenReturn(Optional.of(current.getProprietary()));
        when(restaurantGateway.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateRestaurantInput input = new UpdateRestaurantInput(10L, "Nome Atualizado", null, KichenTypeEnum.BRAZILIAN,
                current.getStartOperationTime(), current.getEndOperationTime(), 1L);

        Optional<Restaurant> out = useCase.execute(input);
        assertTrue(out.isPresent());
        Restaurant r = out.get();
        assertAll(
                () -> assertEquals(10L, r.getId()),
                () -> assertEquals("Nome Atualizado", r.getName()),
                () -> assertEquals(KichenTypeEnum.BRAZILIAN, r.getKitchenType())
        );
    }
}
