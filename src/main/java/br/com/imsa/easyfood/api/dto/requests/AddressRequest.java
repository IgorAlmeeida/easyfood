package br.com.imsa.easyfood.api.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AddressRequest", description = "Request payload with user address information")
public class AddressRequest {

    @Schema(description = "Street name", example = "Av. Paulista")
    @NotBlank(message = "{validation.address.street.required}")
    private String street; // rua

    @Schema(description = "Neighborhood", example = "Bela Vista")
    @NotBlank(message = "{validation.address.neighborhood.required}")
    private String neighborhood; // bairro

    @Schema(description = "City", example = "São Paulo")
    @NotBlank(message = "{validation.address.city.required}")
    private String city; // cidade

    @Schema(description = "House or building number", example = "1000")
    @NotBlank(message = "{validation.address.number.required}")
    private String number; // numero

    @Schema(description = "ZIP/Postal code", example = "01310100")
    @NotBlank(message = "{validation.address.zipcode.required}")
    private String zipCode; // cep
}
