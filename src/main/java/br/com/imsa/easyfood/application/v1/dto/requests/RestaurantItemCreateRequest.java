package br.com.imsa.easyfood.application.v1.dto.requests;

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

    @NotBlank
    private String description;

    @NotNull
    private Double price;

    private String image;
}
