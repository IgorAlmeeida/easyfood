package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantItemTest {

    @Test
    @DisplayName("Deve criar RestaurantItem com sucesso (construtor com id)")
    void shouldCreateWithId() {
        Restaurant restaurant = null; // opcional
        RestaurantItem item = new RestaurantItem(1L, "Pizza Margherita", 39.9, "img.png", AvailabilityEnum.DELIVERY, restaurant);
        assertAll(
                () -> assertEquals(1L, item.getId()),
                () -> assertEquals("Pizza Margherita", item.getDescription()),
                () -> assertEquals(39.9, item.getPrice()),
                () -> assertEquals("img.png", item.getImage()),
                () -> assertEquals(AvailabilityEnum.DELIVERY, item.getAvailability()),
                () -> assertNull(item.getRestaurant())
        );
    }

    @Test
    @DisplayName("Deve criar RestaurantItem com sucesso (construtor sem id)")
    void shouldCreateWithoutId() {
        Restaurant restaurant = null; // opcional
        RestaurantItem item = new RestaurantItem("Suco", 7.5, null, AvailabilityEnum.LOCAL, restaurant);
        assertAll(
                () -> assertNull(item.getId()),
                () -> assertEquals("Suco", item.getDescription()),
                () -> assertEquals(7.5, item.getPrice()),
                () -> assertNull(item.getImage()),
                () -> assertEquals(AvailabilityEnum.LOCAL, item.getAvailability()),
                () -> assertNull(item.getRestaurant())
        );
    }

    @Nested
    class ValidationTests {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" \t", "\n"})
        @DisplayName("Deve falhar quando descrição é inválida")
        void shouldFailWhenDescriptionInvalid(String description) {
            NegocioException ex = assertThrows(NegocioException.class,
                    () -> new RestaurantItem(description, 10.0, null, AvailabilityEnum.DELIVERY, null));
            assertEquals("A descrição do item é obrigatória.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve falhar quando preço é null")
        void shouldFailWhenPriceNull() {
            NegocioException ex = assertThrows(NegocioException.class,
                    () -> new RestaurantItem("Item", null, null, AvailabilityEnum.DELIVERY, null));
            assertEquals("O preço é obrigatório.", ex.getMessage());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, -1.0, -0.01})
        @DisplayName("Deve falhar quando preço é menor ou igual a zero")
        void shouldFailWhenPriceNotPositive(double price) {
            NegocioException ex = assertThrows(NegocioException.class,
                    () -> new RestaurantItem("Item", price, null, AvailabilityEnum.DELIVERY, null));
            assertEquals("O preço deve ser maior que zero.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve falhar quando disponibilidade é null")
        void shouldFailWhenAvailabilityNull() {
            NegocioException ex = assertThrows(NegocioException.class,
                    () -> new RestaurantItem("Item", 10.0, null, null, null));
            assertEquals("A disponibilidade é obrigatória.", ex.getMessage());
        }
    }
}
