package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.exception.NegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    @DisplayName("Deve instanciar UserType com e sem id")
    void instantiation() {
        UserType u1 = new UserType("ADMIN");
        assertAll(
                () -> assertNull(u1.getId()),
                () -> assertEquals("ADMIN", u1.getName())
        );
        UserType u2 = new UserType(5L, "CLIENT");
        assertAll(
                () -> assertEquals(5L, u2.getId()),
                () -> assertEquals("CLIENT", u2.getName())
        );
    }

    @Test
    @DisplayName("Deve lançar quando nome é null ou blank")
    void nameValidation() {
        NegocioException e1 = assertThrows(NegocioException.class, () -> new UserType((String) null));
        assertEquals("O nome do tipo de usuário é obrigatório.", e1.getMessage());
        NegocioException e2 = assertThrows(NegocioException.class, () -> new UserType(" "));
        assertEquals("O nome do tipo de usuário é obrigatório.", e2.getMessage());
        NegocioException e3 = assertThrows(NegocioException.class, () -> new UserType(1L, " "));
        assertEquals("O nome do tipo de usuário é obrigatório.", e3.getMessage());
    }
}
