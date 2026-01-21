package br.com.imsa.easyfood.domain.usercase.restauranteitem;

import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import br.com.imsa.easyfood.domain.gateway.RestaurantItemGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantItemUseCaseTest {

    @Mock
    private RestaurantItemGateway itemGateway;

    @InjectMocks
    private DeleteRestaurantItemUseCase useCase;

    @Test
    @DisplayName("Deve retornar false quando item não encontrado")
    void notFound() {
        when(itemGateway.findById(1L)).thenReturn(Optional.empty());
        assertFalse(useCase.execute(1L));
        verify(itemGateway, never()).delete(any());
    }

    @Test
    @DisplayName("Deve deletar quando encontrado e retornar true")
    void foundAndDelete() {
        RestaurantItem item = new RestaurantItem(1L, "I", 1.0, null, AvailabilityEnum.DELIVERY, null);
        when(itemGateway.findById(1L)).thenReturn(Optional.of(item));
        assertTrue(useCase.execute(1L));
        verify(itemGateway).delete(item);
    }
}
