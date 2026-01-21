package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.exception.NegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSystemTest {

    private Address addr() { return new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000"); }
    private UserType type() { return new UserType(1L, "ADMIN"); }

    @Test
    @DisplayName("Deve instanciar UserSystem com e sem id (dois construtores)")
    void instantiation() {
        UserSystem u1 = new UserSystem("Nome", "a@b.com", "user", "secret1", true, type(), addr());
        assertAll(
                () -> assertNull(u1.getId()),
                () -> assertEquals("Nome", u1.getName()),
                () -> assertTrue(u1.isActive())
        );
        UserSystem u2 = new UserSystem(2L, "Nome", "a@b.com", "user", "secret1", true, type(), addr());
        assertAll(
                () -> assertEquals(2L, u2.getId()),
                () -> assertEquals("user", u2.getUsername())
        );
    }

    @Nested
    class Validations {
        @Test
        @DisplayName("Deve lançar quando nome é null ou blank")
        void nameValidation() {
            NegocioException e1 = assertThrows(NegocioException.class, () -> new UserSystem(null, "a@b.com", "u", "secret1", true, type(), addr()));
            assertEquals("O campo nome é obrigatório.", e1.getMessage());
            NegocioException e2 = assertThrows(NegocioException.class, () -> new UserSystem(" ", "a@b.com", "u", "secret1", true, type(), addr()));
            assertEquals("O campo nome é obrigatório.", e2.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando email é null, blank ou inválido")
        void emailValidation() {
            NegocioException e1 = assertThrows(NegocioException.class, () -> new UserSystem("Nome", null, "u", "secret1", true, type(), addr()));
            assertEquals("O campo e-mail é obrigatório.", e1.getMessage());
            NegocioException e2 = assertThrows(NegocioException.class, () -> new UserSystem("Nome", " ", "u", "secret1", true, type(), addr()));
            assertEquals("O campo e-mail é obrigatório.", e2.getMessage());
            NegocioException e3 = assertThrows(NegocioException.class, () -> new UserSystem("Nome", "invalido", "u", "secret1", true, type(), addr()));
            assertEquals("E-mail inválido.", e3.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando username é null ou blank")
        void usernameValidation() {
            NegocioException e1 = assertThrows(NegocioException.class, () -> new UserSystem("Nome", "a@b.com", null, "secret1", true, type(), addr()));
            assertEquals("O campo username é obrigatório.", e1.getMessage());
            NegocioException e2 = assertThrows(NegocioException.class, () -> new UserSystem("Nome", "a@b.com", " ", "secret1", true, type(), addr()));
            assertEquals("O campo username é obrigatório.", e2.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando senha é null, blank ou pequena")
        void passwordValidation() {
            NegocioException e1 = assertThrows(NegocioException.class, () -> new UserSystem("Nome", "a@b.com", "u", null, true, type(), addr()));
            assertEquals("O campo senha é obrigatório.", e1.getMessage());
            NegocioException e2 = assertThrows(NegocioException.class, () -> new UserSystem("Nome", "a@b.com", "u", " ", true, type(), addr()));
            assertEquals("O campo senha é obrigatório.", e2.getMessage());
            NegocioException e3 = assertThrows(NegocioException.class, () -> new UserSystem("Nome", "a@b.com", "u", "12345", true, type(), addr()));
            assertEquals("A senha deve conter ao menos 6 caracteres.", e3.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando userType é null")
        void userTypeValidation() {
            NegocioException e = assertThrows(NegocioException.class, () -> new UserSystem("Nome", "a@b.com", "u", "secret1", true, null, addr()));
            assertEquals("O tipo de usuário é obrigatório.", e.getMessage());
        }

        @Test
        @DisplayName("Deve lançar quando address é null")
        void addressValidation() {
            NegocioException e = assertThrows(NegocioException.class, () -> new UserSystem("Nome", "a@b.com", "u", "secret1", true, type(), null));
            assertEquals("O endereço é obrigatório.", e.getMessage());
        }

        @Test
        @DisplayName("Cobrir as mesmas validações no construtor com id")
        void validationsWithIdConstructor() {
            NegocioException e1 = assertThrows(NegocioException.class, () -> new UserSystem(1L, null, "a@b.com", "u", "secret1", true, type(), addr()));
            assertEquals("O campo nome é obrigatório.", e1.getMessage());
        }
    }
}
