package br.com.imsa.easyfood.domain.usercase.usertype;

import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
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
class DeleteUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway gateway;

    @InjectMocks
    private DeleteUserTypeUseCase useCase;

    @Test
    @DisplayName("Deve retornar true e chamar delete quando encontrado")
    void foundAndDelete() {
        UserType t = new UserType(1L, "ADMIN");
        when(gateway.findById(1L)).thenReturn(Optional.of(t));
        boolean res = useCase.execute(1L);
        assertTrue(res);
        ArgumentCaptor<UserType> cap = ArgumentCaptor.forClass(UserType.class);
        verify(gateway).delete(cap.capture());
        assertEquals(1L, cap.getValue().getId());
    }

    @Test
    @DisplayName("Deve retornar false quando não encontrado")
    void notFound() {
        when(gateway.findById(1L)).thenReturn(Optional.empty());
        boolean res = useCase.execute(1L);
        assertFalse(res);
        verify(gateway, never()).delete(any());
    }
}
