package br.com.imsa.easyfood.application.v1.mappers;

import br.com.imsa.easyfood.application.v1.dto.responses.RestaurantResponse;
import br.com.imsa.easyfood.domain.dto.output.restaurant.CreateRestaurantOutput;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressMapperApp.class, UserSystemMapperApp.class})
public interface RestaurantMapperApp {

    RestaurantResponse toRestaurantResponse(CreateRestaurantOutput restaurant);

    RestaurantResponse toRestaurantResponse(Restaurant restaurant);
}
