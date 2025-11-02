package br.com.imsa.easyfood.api.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private Long id;
    private String street; // rua
    private String neighborhood; // bairro
    private String city; // cidade
    private String number; // numero
    private String zipCode; // cep
}
