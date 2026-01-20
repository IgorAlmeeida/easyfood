package br.com.imsa.easyfood.domain.dto.output.address;

public record CreateAddressOutput(
        Long id,
        String street,
        String neighborhood,
        String city,
        String number,
        String zipCode
) {
}
