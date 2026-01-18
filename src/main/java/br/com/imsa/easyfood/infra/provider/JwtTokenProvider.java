package br.com.imsa.easyfood.infra.provider;

import br.com.imsa.easyfood.infra.config.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {
    private final JwtUtils  jwtUtils;

    @Override
    public String generate(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }
}
