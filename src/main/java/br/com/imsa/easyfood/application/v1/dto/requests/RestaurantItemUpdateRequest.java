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

    private String description;

    private Double price;

    private String image;

    private AvailabilityEnum availability;

    private Long restaurantId;
}
