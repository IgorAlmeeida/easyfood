package br.com.imsa.easyfood.domain.usercase.usertype;

import br.com.imsa.easyfood.domain.dto.input.usertype.CreateUserTypeInput;
import br.com.imsa.easyfood.domain.dto.output.usertype.CreateUserTypeOutput;
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
class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @InjectMocks
    private CreateUserTypeUseCase useCase;

    @Test
    @DisplayName("Deve retornar Optional.empty quando input é null")
    void nullInput() {
        Optional<CreateUserTypeOutput> out = useCase.execute(null);
        assertTrue(out.isEmpty());
    }

    @Test
    @DisplayName("Happy path: deve criar UserType e retornar output")
    void happyPath() {
        when(userTypeGateway.save(any(UserType.class))).thenAnswer(inv -> {
            UserType arg = inv.getArgument(0);
            return new UserType(10L, arg.getName());
        });
        Optional<CreateUserTypeOutput> out = useCase.execute(new CreateUserTypeInput("ADMIN"));
        assertTrue(out.isPresent());
        CreateUserTypeOutput o = out.get();
        assertAll(
                () -> assertEquals(10L, o.id()),
                () -> assertEquals("ADMIN", o.name())
        );
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando save retorna null")
    void saveReturnsNull() {
        when(userTypeGateway.save(any(UserType.class))).thenReturn(null);
        Optional<CreateUserTypeOutput> out = useCase.execute(new CreateUserTypeInput("CLIENT"));
        assertTrue(out.isEmpty());
    }
}
