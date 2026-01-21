package br.com.imsa.easyfood.application.v1.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RestaurantItemResponse", description = "Restaurant item API response")
public class RestaurantItemResponse {
    @Schema(description = "Item identifier", example = "10")
    private Long id;

    @Schema(description = "Item description", example = "Cheeseburger")
    private String description;

    @Schema(description = "Item price", example = "29.90")
    private Double price;

    @Schema(description = "URL or path of the item image", example = "https://cdn.example.com/img/cheeseburger.png")
    private String image;
}