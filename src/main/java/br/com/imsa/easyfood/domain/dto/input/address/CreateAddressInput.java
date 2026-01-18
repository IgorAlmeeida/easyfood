package br.com.imsa.easyfood.domain.dto.input.address;

public record CreateAddressInput (
        String street,
        String neighborhood,
        String city,
        String number,
        String zipCode
) {
}
