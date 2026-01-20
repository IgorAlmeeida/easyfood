package br.com.imsa.easyfood.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantItem {

    private Long id;

    private String description;

    private Double price;

    //falta enum type Diponibildiade

    private String image;
}
