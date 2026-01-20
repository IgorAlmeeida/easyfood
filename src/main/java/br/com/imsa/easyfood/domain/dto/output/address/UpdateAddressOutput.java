package br.com.imsa.easyfood.domain.dto.output.address;


public record UpdateAddressOutput(
        Long id,
        String street,
        String neighborhood,
        String city,
        String number,
        String zipCode
) {
}
