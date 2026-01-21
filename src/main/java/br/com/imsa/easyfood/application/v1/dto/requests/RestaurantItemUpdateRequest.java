package br.com.imsa.easyfood.application.v1.dto.requests;

import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RestaurantItemUpdateRequest", description = "Request payload to update a restaurant item")
public class RestaurantItemUpdateRequest {

    @Schema(description = "Item description", example = "Veggie Pizza")
    private String description;

    @Schema(description = "Item price", example = "49.90")
    private Double price;

    @Schema(description = "URL or path of the item image", example = "https://cdn.example.com/img/veggie-pizza.png")
    private String image;

    @Schema(description = "Availability status of the item", example = "UNAVAILABLE")
    private AvailabilityEnum availability;

    @Schema(description = "Associated restaurant identifier", example = "2")
    private Long restaurantId;
}
