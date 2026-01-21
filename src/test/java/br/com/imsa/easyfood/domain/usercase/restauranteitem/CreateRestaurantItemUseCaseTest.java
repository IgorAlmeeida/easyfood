package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.dto.input.restaurantitem.CreateRestaurantItemInput;
import br.com.imsa.easyfood.domain.dto.output.restaurantitem.CreateRestaurantItemOutput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import br.com.imsa.easyfood.domain.gateway.RestaurantItemGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantItemUseCaseTest {

    @Mock
    private RestaurantItemGateway itemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private CreateRestaurantItemUseCase useCase;

    private Restaurant buildRestaurant() {
        Address address = new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000");
        UserType userType = new UserType(1L, "ADMIN");
        UserSystem prop = new UserSystem(1L, "Nome", "a@b.com", "user", "secret1", true, userType, address);
        return new Restaurant(10L, "Ristorante", address, KichenTypeEnum.ITALIAN,
                LocalDateTime.of(2026,1,1,9,0), LocalDateTime.of(2026,1,1,18,0), prop);
    }

    private CreateRestaurantItemInput input() {
        return new CreateRestaurantItemInput("Pizza", 30.0, "img.png", AvailabilityEnum.DELIVERY, 10L);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando input é null")
    void nullInput() {
        Optional<CreateRestaurantItemOutput> out = useCase.execute(null);
        assertTrue(out.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar quando restaurante não encontrado")
    void restaurantNotFound() {
        when(restaurantGateway.findById(10L)).thenReturn(Optional.empty());
        NegocioException e = assertThrows(NegocioException.class, () -> useCase.execute(input()));
        assertEquals("Restaurante não encontrado no sistema.", e.getMessage());
    }

    @Test
    @DisplayName("Happy path: deve criar item e retornar output com dados do item salvo")
    void happyPath() {
        when(restaurantGateway.findById(10L)).thenReturn(Optional.of(buildRestaurant()));
        when(itemGateway.save(any(RestaurantItem.class))).thenAnswer(inv -> {
            RestaurantItem arg = inv.getArgument(0);
            return new RestaurantItem(100L, arg.getDescription(), arg.getPrice(), arg.getImage(), arg.getAvailability(), arg.getRestaurant());
        });
        Optional<CreateRestaurantItemOutput> out = useCase.execute(input());
        assertTrue(out.isPresent());
        CreateRestaurantItemOutput o = out.get();
        assertAll(
                () -> assertEquals(100L, o.id()),
                () -> assertEquals("Pizza", o.description()),
                () -> assertEquals(30.0, o.price()),
                () -> assertEquals("img.png", o.image())
        );
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando gateway retorna null ao salvar")
    void saveReturnsNull() {
        when(restaurantGateway.findById(10L)).thenReturn(Optional.of(buildRestaurant()));
        when(itemGateway.save(any(RestaurantItem.class))).thenReturn(null);
        Optional<CreateRestaurantItemOutput> out = useCase.execute(input());
        assertTrue(out.isEmpty());
    }
}
