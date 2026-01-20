package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.exception.NegocioException;
import lombok.Getter;

@Getter
public class UserSystem {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private boolean active;
    private UserType userType;
    private Address address;

    public UserSystem(Long id,
                      String name,
                      String email,
                      String username,
                      String password,
                      boolean active,
                      UserType userType,
                      Address address) {

        if (name == null || name.isBlank()) {
            throw new NegocioException("O campo nome é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new NegocioException("O campo e-mail é obrigatório.");
        }
        if (!email.matches("^[^@\\n\\r]+@[^@\\n\\r]+\\.[^@\\n\\r]+$")) {
            throw new NegocioException("E-mail inválido.");
        }
        if (username == null || username.isBlank()) {
            throw new NegocioException("O campo username é obrigatório.");
        }
        if (password == null || password.isBlank()) {
            throw new NegocioException("O campo senha é obrigatório.");
        }
        if (password.length() < 6) {
            throw new NegocioException("A senha deve conter ao menos 6 caracteres.");
        }
        if (userType == null) {
            throw new NegocioException("O tipo de usuário é obrigatório.");
        }
        if (address == null) {
            throw new NegocioException("O endereço é obrigatório.");
        }

        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = active;
        this.userType = userType;
        this.address = address;
    }

    public UserSystem(String name,
                      String email,
                      String username,
                      String password,
                      boolean active,
                      UserType userType,
                      Address address) {

        if (name == null || name.isBlank()) {
            throw new NegocioException("O campo nome é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new NegocioException("O campo e-mail é obrigatório.");
        }
        if (!email.matches("^[^@\\n\\r]+@[^@\\n\\r]+\\.[^@\\n\\r]+$")) {
            throw new NegocioException("E-mail inválido.");
        }
        if (username == null || username.isBlank()) {
            throw new NegocioException("O campo username é obrigatório.");
        }
        if (password == null || password.isBlank()) {
            throw new NegocioException("O campo senha é obrigatório.");
        }
        if (password.length() < 6) {
            throw new NegocioException("A senha deve conter ao menos 6 caracteres.");
        }
        if (userType == null) {
            throw new NegocioException("O tipo de usuário é obrigatório.");
        }
        if (address == null) {
            throw new NegocioException("O endereço é obrigatório.");
        }

        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = active;
        this.userType = userType;
        this.address = address;
    }
}
