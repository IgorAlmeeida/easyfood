package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.exception.NegocioException;
import lombok.Getter;

@Getter
public class UserType {

    private Long id;
    private String name;

    public UserType(Long id, String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    public UserType(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new NegocioException("O nome do tipo de usuário é obrigatório.");
        }
    }
}
