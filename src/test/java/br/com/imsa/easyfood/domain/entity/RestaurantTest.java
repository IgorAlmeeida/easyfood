package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private LocalDateTime now() {
        return LocalDateTime.of(2026, 1, 1, 9, 0);
    }

    @Test
    @DisplayName("Deve instanciar Restaurant com e sem campos opcionais")
    void instantiation() {
        LocalDateTime start = now();
        LocalDateTime end = start.plusHours(8);
        // address e proprietary podem ser null
        Restaurant r1 = new Restaurant(1L, "Ristorante", null, KichenTypeEnum.ITALIAN, start, end, null);
        assertAll(
                () -> assertEquals(1L, r1.getId()),
                () -> assertEquals("Ristorante", r1.getName()),
                () -> assertNull(r1.getAddress()),
                () -> assertNull(r1.getProprietary())
        );

        Address addr = new Address(10L, "Rua A", "Centro", "Cidade", "100", "00000-000");
        UserType ut = new UserType(5L, "ADMIN");
        UserSystem prop = new UserSystem(2L, "João", "j@x.com", "joao", "secret1", true, ut, addr);
        Restaurant r2 = new Restaurant("Cantina", addr, KichenTypeEnum.BRAZILIAN, start, end, prop);
        assertAll(
                () -> assertNull(r2.getId()),
                () -> assertEquals("Cantina", r2.getName()),
                () -> assertNotNull(r2.getAddress()),
                () -> assertNotNull(r2.getProprietary())
        );
    }

    @Nested
    class Validations {
        @Test
        @DisplayName("Deve lançar quando nome é null ou blank")
        void nameValidation() {
            LocalDateTime start = now();
            LocalDateTime end = start.plusHours(1);
            NegocioException e1 = assertThrows(NegocioException.class,
                    () -> new Restaurant(null, null, KichenTypeEnum.ITALIAN, start, end, null));
            assertEquals("O nome do restaurante é obrigatório.", e1.getMessage());
            NegocioException e2 = assertThrows(NegocioException.class,
                    () -> new Restaurant(" ", null, KichenTypeEnum.ITALIAN, start, end, null));
            assertEquals("O nome do restaurante é obrigatório.", e2.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando tipo de cozinha é null")
        void kitchenValidation() {
            LocalDateTime start = now();
            LocalDateTime end = start.plusHours(1);
            NegocioException e = assertThrows(NegocioException.class,
                    () -> new Restaurant("Ok", null, null, start, end, null));
            assertEquals("O tipo de cozinha é obrigatório.", e.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando horário de abertura é null")
        void startTimeValidation() {
            LocalDateTime end = now().plusHours(1);
            NegocioException e = assertThrows(NegocioException.class,
                    () -> new Restaurant("Ok", null, KichenTypeEnum.ITALIAN, null, end, null));
            assertEquals("O horário de abertura é obrigatório.", e.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando horário de fechamento é null")
        void endTimeValidation() {
            LocalDateTime start = now();
            NegocioException e = assertThrows(NegocioException.class,
                    () -> new Restaurant("Ok", null, KichenTypeEnum.ITALIAN, start, null, null));
            assertEquals("O horário de fechamento é obrigatório.", e.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando fechamento é antes da abertura")
        void endBeforeStart() {
            LocalDateTime start = now();
            LocalDateTime end = start.minusMinutes(1);
            NegocioException e = assertThrows(NegocioException.class,
                    () -> new Restaurant("Ok", null, KichenTypeEnum.ITALIAN, start, end, null));
            assertEquals("O horário de fechamento deve ser após o horário de abertura.", e.getMessage());
        }
    }
}
