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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchRestaurantItemUseCaseTest {

    @Mock
    private RestaurantItemGateway itemGateway;

    @InjectMocks
    private SearchRestaurantItemUseCase useCase;

    private RestaurantItem itemSample() {
        return new RestaurantItem(1L, "Item", 10.0, null, AvailabilityEnum.DELIVERY, null);
        
    }

    @Test
    @DisplayName("execute(pageable) deve delegar para findAll")
    void executePageable() {
        Pageable p = PageRequest.of(0, 5);
        Page<RestaurantItem> pg = new PageImpl<>(List.of(itemSample()));
        when(itemGateway.findAll(p)).thenReturn(pg);
        Page<RestaurantItem> out = useCase.execute(p);
        assertEquals(1, out.getTotalElements());
        verify(itemGateway).findAll(p);
    }

    @Test
    @DisplayName("execute(pageable, description null/blank) deve delegar para findAll")
    void executeNullBlankDescription() {
        Pageable p = PageRequest.of(0, 5);
        when(itemGateway.findAll(p)).thenReturn(Page.empty());
        assertEquals(0, useCase.execute(p, null).getTotalElements());
        assertEquals(0, useCase.execute(p, " ").getTotalElements());
        verify(itemGateway, times(2)).findAll(p);
        verify(itemGateway, never()).findByDescriptionContainingIgnoreCase(any(), any());
    }

    @Test
    @DisplayName("execute(pageable, description) deve delegar para findByDescriptionContainingIgnoreCase")
    void executeWithDescription() {
        Pageable p = PageRequest.of(0, 5);
        Page<RestaurantItem> pg = new PageImpl<>(List.of(itemSample()));
        when(itemGateway.findByDescriptionContainingIgnoreCase("pizza", p)).thenReturn(pg);
        Page<RestaurantItem> out = useCase.execute(p, "pizza");
        assertEquals(1, out.getTotalElements());
        verify(itemGateway).findByDescriptionContainingIgnoreCase("pizza", p);
    }

    @Test
    @DisplayName("execute(pageable, restaurantId, description) – ambas presentes")
    void executeRestaurantAndDescription() {
        Pageable p = PageRequest.of(0, 5);
        when(itemGateway.findByRestaurantIdAndDescriptionContainingIgnoreCase(9L, "pizza", p))
                .thenReturn(new PageImpl<>(List.of(itemSample())));
        Page<RestaurantItem> out = useCase.execute(p, 9L, "pizza");
        assertEquals(1, out.getTotalElements());
        verify(itemGateway).findByRestaurantIdAndDescriptionContainingIgnoreCase(9L, "pizza", p);
    }

    @Test
    @DisplayName("execute(pageable, restaurantId, description) – apenas restaurantId")
    void executeOnlyRestaurant() {
        Pageable p = PageRequest.of(0, 5);
        when(itemGateway.findByRestaurantId(9L, p)).thenReturn(Page.empty());
        Page<RestaurantItem> out = useCase.execute(p, 9L, " ");
        assertEquals(0, out.getTotalElements());
        verify(itemGateway).findByRestaurantId(9L, p);
    }

    @Test
    @DisplayName("execute(pageable, restaurantId, description) – apenas description")
    void executeOnlyDescription() {
        Pageable p = PageRequest.of(0, 5);
        when(itemGateway.findByDescriptionContainingIgnoreCase("s", p)).thenReturn(Page.empty());
        Page<RestaurantItem> out = useCase.execute(p, null, "s");
        assertEquals(0, out.getTotalElements());
        verify(itemGateway).findByDescriptionContainingIgnoreCase("s", p);
    }

    @Test
    @DisplayName("execute(pageable, restaurantId, description) – nenhum filtro")
    void executeNoFilter() {
        Pageable p = PageRequest.of(0, 5);
        when(itemGateway.findAll(p)).thenReturn(Page.empty());
        Page<RestaurantItem> out = useCase.execute(p, null, " ");
        assertEquals(0, out.getTotalElements());
        verify(itemGateway).findAll(p);
    }

    @Test
    @DisplayName("findById deve delegar para gateway")
    void findById() {
        when(itemGateway.findById(1L)).thenReturn(Optional.of(itemSample()));
        assertTrue(useCase.findById(1L).isPresent());
    }
}
