package br.com.imsa.easyfood.domain.usercase.auth;

import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
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
class ChangePasswordUserCaseTest {

    @Mock private UserSystemGateway userSystemGateway;
    @InjectMocks private ChangePasswordUserCase useCase;

    private UserSystem user() {
        Address addr = new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000");
        UserType type = new UserType(1L, "ADMIN");
        return new UserSystem(9L, "Nome", "a@b.com", "user", "oldpwd", true, type, addr);
    }

    @Test
    @DisplayName("Deve retornar false quando usuário não encontrado")
    void userNotFound() {
        when(userSystemGateway.findById(9L)).thenReturn(Optional.empty());
        boolean res = useCase.execute(9L, "oldpwd", "newpwd");
        assertFalse(res);
        verify(userSystemGateway, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar a senha e retornar true quando usuário encontrado")
    void updatePassword() {
        when(userSystemGateway.findById(9L)).thenReturn(Optional.of(user()));
        boolean res = useCase.execute(9L, "oldpwd", "newpwd");
        assertTrue(res);
        ArgumentCaptor<UserSystem> cap = ArgumentCaptor.forClass(UserSystem.class);
        verify(userSystemGateway).save(cap.capture());
        assertEquals("newpwd", cap.getValue().getPassword());
    }
}
