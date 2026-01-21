package br.com.imsa.easyfood.application.v1.dto.requests;

import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "RestaurantItemCreateRequest", description = "Request payload to create a restaurant item")
public class RestaurantItemCreateRequest {

    @Schema(description = "Item description", example = "Cheeseburger")
    @NotBlank
    private String description;

    @Schema(description = "Item price", example = "29.90")
    @NotNull
    private Double price;

    @Schema(description = "URL or path of the item image", example = "https://cdn.example.com/img/cheeseburger.png")
    private String image;

    @Schema(description = "Availability status of the item", example = "AVAILABLE")
    private AvailabilityEnum availability;

    @Schema(description = "Associated restaurant identifier", example = "1")
    private Long restaurantId;
}
