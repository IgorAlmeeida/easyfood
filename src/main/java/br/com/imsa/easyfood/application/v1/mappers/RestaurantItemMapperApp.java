package br.com.imsa.easyfood.application.v1.mappers;

import br.com.imsa.easyfood.application.v1.dto.responses.RestaurantItemResponse;
import br.com.imsa.easyfood.domain.dto.output.restaurantitem.CreateRestaurantItemOutput;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantItemMapperApp {

    RestaurantItemResponse toRestaurantItemResponse(CreateRestaurantItemOutput item);

    RestaurantItemResponse toRestaurantItemResponse(RestaurantItem item);
}
