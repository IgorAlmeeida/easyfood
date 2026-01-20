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
    private Long id;
    private String description;
    private Double price;
    private String image;
}