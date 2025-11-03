package br.com.imsa.easyfood.api.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AddressResponse", description = "Address information associated with the user")
public class AddressResponse {
    @Schema(description = "Address identifier", example = "1")
    private Long id;
    @Schema(description = "Street name", example = "Av. Paulista")
    private String street; // rua
    @Schema(description = "Neighborhood", example = "Bela Vista")
    private String neighborhood; // bairro
    @Schema(description = "City", example = "São Paulo")
    private String city; // cidade
    @Schema(description = "House or building number", example = "1000")
    private String number; // numero
    @Schema(description = "ZIP/Postal code", example = "01310100")
    private String zipCode; // cep
}
