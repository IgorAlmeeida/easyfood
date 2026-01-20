package br.com.imsa.easyfood.infra.provider;

import br.com.imsa.easyfood.infra.config.jwt.JwtUtils;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider, UserDetailsService {

    private final JwtUtils  jwtUtils;
    private final UserSystemRepository repository;

    @Override
    public String generate(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
