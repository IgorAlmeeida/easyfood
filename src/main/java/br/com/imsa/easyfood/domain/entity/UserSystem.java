package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.infra.enums.UserTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSystem {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private boolean active;
    private UserTypeEnum userType;
    private Address address;
}
