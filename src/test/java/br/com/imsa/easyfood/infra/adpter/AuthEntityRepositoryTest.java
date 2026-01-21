package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.exception.NegocioException;
import br.com.imsa.easyfood.infra.provider.TokenProvider;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthEntityRepositoryTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserSystemRepository repository;

    @InjectMocks
    private AuthEntityRepository sut;

    @Test
    @DisplayName("login com credenciais válidas deve autenticar e gerar token")
    void login_success() {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generate(auth)).thenReturn("token123");

        String token = sut.login("user", "pwd");

        assertEquals("token123", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generate(auth);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("login com falha de autenticação deve lançar NegocioException com mensagem do MessageSource")
    void login_failureThrowsBusinessException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));
        when(messageSource.getMessage(eq("auth.credentials.bad"), isNull(), any(Locale.class))).thenReturn("Credenciais inválidas");

        NegocioException ex = assertThrows(NegocioException.class, () -> sut.login("user", "wrong"));
        assertEquals("Credenciais inválidas", ex.getMessage());
        verify(messageSource).getMessage(eq("auth.credentials.bad"), isNull(), any(Locale.class));
    }
}
