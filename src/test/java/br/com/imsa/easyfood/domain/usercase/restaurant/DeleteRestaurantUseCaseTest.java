package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
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
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private DeleteRestaurantUseCase useCase;

    @Test
    @DisplayName("Deve retornar false quando id não encontrado")
    void notFound() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.empty());
        assertFalse(useCase.execute(1L));
        verify(restaurantGateway, never()).delete(any());
    }

    @Test
    @DisplayName("Deve deletar quando encontrado e retornar true")
    void foundAndDelete() {
        when(restaurantGateway.findById(2L)).thenReturn(Optional.of(mock(Restaurant.class)));
        assertTrue(useCase.execute(2L));
        verify(restaurantGateway).delete(any(Restaurant.class));
    }
}
