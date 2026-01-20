package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.exception.NegocioException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Address {
    private Long id;
    private String street;
    private String neighborhood;
    private String city;
    private String number;
    private String zipCode;

    public Address(String street,
                   String neighborhood,
                   String city,
                   String number,
                   String zipCode) {
        if (street == null || street.isBlank()) {
            throw new NegocioException("O campo rua é obrigatório.");
        }
        if (neighborhood == null || neighborhood.isBlank()) {
            throw new NegocioException("O bairro é obrigatório.");
        }
        if (city == null || city.isBlank()) {
            throw new NegocioException("A cidade é obrigatória.");
        }
        if (number == null || number.isBlank()) {
            throw new NegocioException("O número é obrigatório.");
        }
        if (zipCode == null || zipCode.isBlank()) {
            throw new NegocioException("O CEP é obrigatório.");
        }
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.number = number;
        this.zipCode = zipCode;
    }

    public Address(Long id,
                   String street,
                   String neighborhood,
                   String city,
                   String number,
                   String zipCode) {
        this(street, neighborhood, city, number, zipCode);
        this.id = id;
    }
}
