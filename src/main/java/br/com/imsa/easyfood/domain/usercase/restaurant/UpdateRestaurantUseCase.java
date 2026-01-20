package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.dto.input.restaurant.UpdateRestaurantInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class UpdateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;
    private final AddressGateway addressGateway;
    private final UserSystemGateway userSystemGateway;

    public Optional<Restaurant> execute(UpdateRestaurantInput input) {
        if (input == null || input.id() == null) return Optional.empty();

        Optional<Restaurant> opt = restaurantGateway.findById(input.id());
        if (opt.isEmpty()) return Optional.empty();

        Restaurant r = opt.get();
        if (input.name() != null) r.setName(input.name());
        if (input.kitchenType() != null) r.setKitchenType(input.kitchenType());
        if (input.startOperationTime() != null) r.setStartOperationTime(input.startOperationTime());
        if (input.endOperationTime() != null) r.setEndOperationTime(input.endOperationTime());

        if (input.addressId() != null) {
            addressGateway.findById(input.addressId()).ifPresent(r::setAddress);
        }
        if (input.proprietaryId() != null) {
            userSystemGateway.findById(input.proprietaryId()).ifPresent(r::setProprietary);
        }

        Restaurant saved = restaurantGateway.save(r);
        return Optional.ofNullable(saved);
    }
}
