package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserSystemUseCaseTest {

    @Mock private UserSystemGateway gateway;
    @InjectMocks private DeleteUserSystemUseCase useCase;

    private UserSystem user() {
        Address addr = new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000");
        UserType type = new UserType(1L, "ADMIN");
        return new UserSystem(7L, "Nome", "a@b.com", "user", "secret1", true, type, addr);
    }

    @Test
    @DisplayName("Deve retornar true e chamar delete quando encontrado")
    void foundAndDelete() {
        when(gateway.findById(7L)).thenReturn(Optional.of(user()));
        boolean res = useCase.execute(7L);
        assertTrue(res);
        ArgumentCaptor<UserSystem> cap = ArgumentCaptor.forClass(UserSystem.class);
        verify(gateway).delete(cap.capture());
        assertEquals(7L, cap.getValue().getId());
    }

    @Test
    @DisplayName("Deve retornar false quando não encontrado")
    void notFound() {
        when(gateway.findById(7L)).thenReturn(Optional.empty());
        boolean res = useCase.execute(7L);
        assertFalse(res);
        verify(gateway, never()).delete(any());
    }
}
