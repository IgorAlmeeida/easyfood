package br.com.imsa.easyfood.domain.usercase.usertype;

import br.com.imsa.easyfood.domain.dto.input.usertype.UpdateUserTypeInput;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway gateway;

    @InjectMocks
    private UpdateUserTypeUseCase useCase;

    @Test
    @DisplayName("Deve retornar Optional.empty quando input é null ou id null")
    void nullInputs() {
        assertTrue(useCase.execute(null).isEmpty());
        assertTrue(useCase.execute(new UpdateUserTypeInput(null, "X")).isEmpty());
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando tipo não encontrado")
    void notFound() {
        when(gateway.findById(1L)).thenReturn(Optional.empty());
        assertTrue(useCase.execute(new UpdateUserTypeInput(1L, "X")).isEmpty());
    }

    @Test
    @DisplayName("Happy path: deve atualizar nome quando informado")
    void happyPath() {
        UserType current = new UserType(1L, "OLD");
        when(gateway.findById(1L)).thenReturn(Optional.of(current));
        when(gateway.save(any(UserType.class))).thenAnswer(inv -> inv.getArgument(0));
        Optional<UserType> out = useCase.execute(new UpdateUserTypeInput(1L, "NEW"));
        assertTrue(out.isPresent());
        assertEquals("NEW", out.get().getName());
    }

    @Test
    @DisplayName("Quando nome for null deve manter nome atual")
    void keepCurrentNameWhenNull() {
        UserType current = new UserType(1L, "CURRENT");
        when(gateway.findById(1L)).thenReturn(Optional.of(current));
        when(gateway.save(any(UserType.class))).thenAnswer(inv -> inv.getArgument(0));
        Optional<UserType> out = useCase.execute(new UpdateUserTypeInput(1L, null));
        assertTrue(out.isPresent());
        assertEquals("CURRENT", out.get().getName());
    }
}
