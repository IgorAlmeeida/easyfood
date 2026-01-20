package br.com.imsa.easyfood.domain.dto.output.address;

import java.time.LocalDateTime;

public record CreateAddressOutput(
        Long id,
        String street,
        String neighborhood,
        String city,
        String number,
        String zipCode,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
