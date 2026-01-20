package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import lombok.Getter;

@Getter
public class RestaurantItem {

    private Long id;
    private String description;
    private Double price;
    private AvailabilityEnum availability;
    private String image;
    private Restaurant restaurant;


    public RestaurantItem(Long id, String description, Double price, String image, AvailabilityEnum availability, Restaurant restaurant) {
        validate(description, price);
        this.id = id;
        this.description = description;
        this.price = price;
        this.image = image;
        this.availability = availability;
        this.restaurant = restaurant;

    }

    public RestaurantItem(String description, Double price, String image, AvailabilityEnum availability, Restaurant restaurant) {
        validate(description, price);
        this.description = description;
        this.price = price;
        this.image = image;
        this.availability = availability;
        this.restaurant = restaurant;
    }

    private void validate(String description, Double price) {
        if (description == null || description.isBlank()) {
            throw new NegocioException("A descrição do item é obrigatória.");
        }
        if (price == null) {
            throw new NegocioException("O preço é obrigatório.");
        }
        if (price <= 0) {
            throw new NegocioException("O preço deve ser maior que zero.");
        }
        if(availability == null) {
            throw new NegocioException("A disponibilidade é obrigatória.");
        }
    }
}
