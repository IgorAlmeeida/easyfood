package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.exception.NegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    @DisplayName("Deve instanciar Address com construtor simples e com id")
    void instantiation() {
        Address a1 = new Address("Rua A", "Centro", "Cidade", "100", "00000-000");
        assertAll(
                () -> assertNull(a1.getId()),
                () -> assertEquals("Rua A", a1.getStreet())
        );

        Address a2 = new Address(10L, "Rua A", "Centro", "Cidade", "100", "00000-000");
        assertAll(
                () -> assertEquals(10L, a2.getId()),
                () -> assertEquals("Centro", a2.getNeighborhood())
        );
    }

    @Nested
    class Validations {
        @Test
        @DisplayName("Deve lançar quando rua é null ou blank")
        void streetValidation() {
            NegocioException ex1 = assertThrows(NegocioException.class, () -> new Address(null, "B", "C", "1", "Z"));
            assertEquals("O campo rua é obrigatório.", ex1.getMessage());
            NegocioException ex2 = assertThrows(NegocioException.class, () -> new Address(" ", "B", "C", "1", "Z"));
            assertEquals("O campo rua é obrigatório.", ex2.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando bairro é null ou blank")
        void neighborhoodValidation() {
            NegocioException ex1 = assertThrows(NegocioException.class, () -> new Address("R", null, "C", "1", "Z"));
            assertEquals("O bairro é obrigatório.", ex1.getMessage());
            NegocioException ex2 = assertThrows(NegocioException.class, () -> new Address("R", " ", "C", "1", "Z"));
            assertEquals("O bairro é obrigatório.", ex2.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando cidade é null ou blank")
        void cityValidation() {
            NegocioException ex1 = assertThrows(NegocioException.class, () -> new Address("R", "B", null, "1", "Z"));
            assertEquals("A cidade é obrigatória.", ex1.getMessage());
            NegocioException ex2 = assertThrows(NegocioException.class, () -> new Address("R", "B", " ", "1", "Z"));
            assertEquals("A cidade é obrigatória.", ex2.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando número é null ou blank")
        void numberValidation() {
            NegocioException ex1 = assertThrows(NegocioException.class, () -> new Address("R", "B", "C", null, "Z"));
            assertEquals("O número é obrigatório.", ex1.getMessage());
            NegocioException ex2 = assertThrows(NegocioException.class, () -> new Address("R", "B", "C", " ", "Z"));
            assertEquals("O número é obrigatório.", ex2.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando CEP é null ou blank")
        void zipValidation() {
            NegocioException ex1 = assertThrows(NegocioException.class, () -> new Address("R", "B", "C", "1", null));
            assertEquals("O CEP é obrigatório.", ex1.getMessage());
            NegocioException ex2 = assertThrows(NegocioException.class, () -> new Address("R", "B", "C", "1", " "));
            assertEquals("O CEP é obrigatório.", ex2.getMessage());
        }
    }
}
