package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private SearchRestaurantUseCase useCase;

    private Restaurant sample() {
        return new Restaurant(1L, "R", null, KichenTypeEnum.ITALIAN, LocalDateTime.now(), LocalDateTime.now().plusHours(1), null);
    }

    @Test
    @DisplayName("execute(pageable) deve delegar para findAll")
    void executePageable() {
        Pageable p = PageRequest.of(0, 10);
        Page<Restaurant> pg = new PageImpl<>(List.of(sample()));
        when(restaurantGateway.findAll(p)).thenReturn(pg);
        Page<Restaurant> out = useCase.execute(p);
        assertEquals(1, out.getTotalElements());
        verify(restaurantGateway).findAll(p);
    }

    @Test
    @DisplayName("execute(pageable, null/blank) deve delegar para findAll")
    void executeNullOrBlank() {
        Pageable p = PageRequest.of(0, 10);
        when(restaurantGateway.findAll(p)).thenReturn(Page.empty());
        assertEquals(0, useCase.execute(p, null).getTotalElements());
        assertEquals(0, useCase.execute(p, " ").getTotalElements());
        verify(restaurantGateway, times(2)).findAll(p);
        verify(restaurantGateway, never()).findByNameContainingIgnoreCase(any(), any());
    }

    @Test
    @DisplayName("execute(pageable, name) deve delegar para findByNameContainingIgnoreCase")
    void executeWithName() {
        Pageable p = PageRequest.of(0, 10);
        Page<Restaurant> pg = new PageImpl<>(List.of(sample()));
        when(restaurantGateway.findByNameContainingIgnoreCase("abc", p)).thenReturn(pg);
        Page<Restaurant> out = useCase.execute(p, "abc");
        assertEquals(1, out.getTotalElements());
        verify(restaurantGateway).findByNameContainingIgnoreCase("abc", p);
    }

    @Test
    @DisplayName("findById deve delegar para gateway")
    void findById() {
        when(restaurantGateway.findById(10L)).thenReturn(Optional.of(sample()));
        assertTrue(useCase.findById(10L).isPresent());
    }
}
