package br.com.imsa.easyfood.infra.provider;

import org.springframework.security.core.Authentication;
public interface TokenProvider {

    String generate(Authentication authentication);

}
