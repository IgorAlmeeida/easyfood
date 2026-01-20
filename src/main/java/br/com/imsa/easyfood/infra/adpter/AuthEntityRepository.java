package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.gateway.AuthGateway;
import br.com.imsa.easyfood.exception.NegocioException;
import br.com.imsa.easyfood.infra.provider.TokenProvider;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthEntityRepository implements AuthGateway {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final MessageSource messageSource;
    private final UserSystemRepository repository;

    @Override
    public String login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return tokenProvider.generate(authentication);
        } catch (Exception e) {
            throw new NegocioException(messageSource.getMessage("auth.credentials.bad", null, LocaleContextHolder.getLocale()));
        }
    }

}
