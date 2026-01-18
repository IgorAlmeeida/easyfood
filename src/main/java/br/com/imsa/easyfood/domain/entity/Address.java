package br.com.imsa.easyfood.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private Long id;
    private String street;
    private String neighborhood;
    private String city;
    private String number;
    private String zipCode;
    private java.time.LocalDateTime createAt;
    private java.time.LocalDateTime updateAt;
}
