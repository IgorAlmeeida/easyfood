package br.com.imsa.easyfood.domain.dto.output.auth;

public record LoginOutput (
        String token,

        String type,
        String username,

        Integer tokenExpiryDuration

){
}
