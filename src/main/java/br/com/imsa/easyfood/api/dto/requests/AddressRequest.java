package br.com.imsa.easyfood.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {

    @NotBlank(message = "É necessário informar a rua.")
    private String street; // rua

    @NotBlank(message = "É necessário informar o bairro.")
    private String neighborhood; // bairro

    @NotBlank(message = "É necessário informar a cidade.")
    private String city; // cidade

    @NotBlank(message = "É necessário informar o número.")
    private String number; // numero

    @NotBlank(message = "É necessário informar o CEP.")
    private String zipCode; // cep
}
