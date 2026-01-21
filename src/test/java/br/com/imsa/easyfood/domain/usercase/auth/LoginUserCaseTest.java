package br.com.imsa.easyfood.domain.usercase.auth;

import br.com.imsa.easyfood.domain.dto.output.auth.LoginOutput;
import br.com.imsa.easyfood.domain.gateway.AuthGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUserCaseTest {

    @Mock private AuthGateway authGateway;
    @InjectMocks private LoginUserCase useCase;

    @Test
    @DisplayName("Deve delegar login ao gateway e retornar LoginOutput com valores esperados")
    void login() {
        when(authGateway.login("user","pass")).thenReturn("TOKEN");
        LoginOutput out = useCase.execute("user","pass");
        assertAll(
                () -> assertEquals("TOKEN", out.token()),
                () -> assertEquals("Bearer", out.type()),
                () -> assertEquals("user", out.username()),
                () -> assertEquals(86400000, out.tokenExpiryDuration())
        );
    }
}
