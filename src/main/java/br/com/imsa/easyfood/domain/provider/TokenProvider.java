package br.com.imsa.easyfood.domain.provider;

import org.springframework.security.core.Authentication;
public interface TokenProvider {

    String generate(Authentication authentication);

}
