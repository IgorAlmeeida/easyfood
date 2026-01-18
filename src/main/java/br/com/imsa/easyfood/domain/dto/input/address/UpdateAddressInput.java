package br.com.imsa.easyfood.domain.dto.input.address;

public record UpdateAddressInput(
        Long id,
        String street,
        String neighborhood,
        String city,
        String number,
        String zipCode
) {
}
