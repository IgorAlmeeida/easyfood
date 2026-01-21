package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.dto.input.restaurantitem.UpdateRestaurantItemInput;
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
class UpdateRestaurantItemUseCaseTest {

    @Mock
    private RestaurantItemGateway itemGateway;
    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private UpdateRestaurantItemUseCase useCase;

    private Restaurant buildRestaurant(Long id) {
        Address address = new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000");
        UserType userType = new UserType(1L, "ADMIN");
        UserSystem prop = new UserSystem(1L, "Nome", "a@b.com", "user", "secret1", true, userType, address);
        return new Restaurant(id, "Ristorante", address, KichenTypeEnum.ITALIAN,
                LocalDateTime.of(2026,1,1,9,0), LocalDateTime.of(2026,1,1,18,0), prop);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando input é null ou id null")
    void nullInputOrId() {
        assertTrue(useCase.execute(null).isEmpty());
        UpdateRestaurantItemInput input = new UpdateRestaurantItemInput(null, null, null, null, null, null);
        assertTrue(useCase.execute(input).isEmpty());
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando item não encontrado")
    void itemNotFound() {
        UpdateRestaurantItemInput input = new UpdateRestaurantItemInput(10L, "Desc", 10.0, null, AvailabilityEnum.DELIVERY, 1L);
        when(itemGateway.findById(10L)).thenReturn(Optional.empty());
        assertTrue(useCase.execute(input).isEmpty());
    }

    @Test
    @DisplayName("Deve lançar quando restaurante não encontrado")
    void restaurantNotFound() {
        RestaurantItem current = new RestaurantItem(10L, "Old", 5.0, null, AvailabilityEnum.LOCAL, null);
        when(itemGateway.findById(10L)).thenReturn(Optional.of(current));
        when(restaurantGateway.findById(999L)).thenReturn(Optional.empty());
        UpdateRestaurantItemInput input = new UpdateRestaurantItemInput(10L, "Novo", 12.0, "img", AvailabilityEnum.DELIVERY, 999L);
        NegocioException ex = assertThrows(NegocioException.class, () -> useCase.execute(input));
        assertEquals("Restaurante não encontrado no sistema.", ex.getMessage());
    }

    @Test
    @DisplayName("Happy path: deve salvar e retornar item atualizado")
    void happyPath() {
        RestaurantItem current = new RestaurantItem(10L, "Old", 5.0, null, AvailabilityEnum.LOCAL, null);
        when(itemGateway.findById(10L)).thenReturn(Optional.of(current));
        Restaurant restaurant = buildRestaurant(2L);
        when(restaurantGateway.findById(2L)).thenReturn(Optional.of(restaurant));
        when(itemGateway.save(any(RestaurantItem.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateRestaurantItemInput input = new UpdateRestaurantItemInput(10L, "Novo", 12.0, "img.png", AvailabilityEnum.DELIVERY, 2L);
        Optional<RestaurantItem> out = useCase.execute(input);
        assertTrue(out.isPresent());
        RestaurantItem item = out.get();
        assertAll(
                () -> assertEquals(10L, item.getId()),
                () -> assertEquals("Novo", item.getDescription()),
                () -> assertEquals(12.0, item.getPrice()),
                () -> assertEquals("img.png", item.getImage()),
                () -> assertEquals(AvailabilityEnum.DELIVERY, item.getAvailability()),
                () -> assertNotNull(item.getRestaurant())
        );
    }
}
