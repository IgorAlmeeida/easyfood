package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.restaurant.CreateRestaurantOutput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.CreateUserSystemOutput;
import br.com.imsa.easyfood.domain.dto.input.restaurant.CreateRestaurantInput;
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
public class CreateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;
    private final AddressGateway addressGateway;
    private final UserSystemGateway userSystemGateway;

    public Optional<CreateRestaurantOutput> execute(CreateRestaurantInput input) {
        if (input == null) return Optional.empty();

        Restaurant r = new Restaurant();
        r.setName(input.name());
        r.setKitchenType(input.kitchenType());
        r.setStartOperationTime(input.startOperationTime());
        r.setEndOperationTime(input.endOperationTime());

        if (input.addressId() != null) {
            addressGateway.findById(input.addressId()).ifPresent(r::setAddress);
        }
        if (input.proprietaryId() != null) {
            userSystemGateway.findById(input.proprietaryId()).ifPresent(r::setProprietary);
        }

        Restaurant saved = restaurantGateway.save(r);
        if (saved == null) return Optional.empty();

        CreateAddressOutput addressOutput = null;
        Address a = saved.getAddress();
        if (a != null) {
            addressOutput = new CreateAddressOutput(
                    a.getId(), a.getStreet(), a.getNeighborhood(), a.getCity(), a.getNumber(), a.getZipCode(), a.getCreateAt(), a.getUpdateAt()
            );
        }

        CreateUserSystemOutput proprietaryOutput = null;
        UserSystem u = saved.getProprietary();
        if (u != null) {
            CreateAddressOutput userAddressOutput = null;
            Address ua = u.getAddress();
            if (ua != null) {
                userAddressOutput = new CreateAddressOutput(
                        ua.getId(), ua.getStreet(), ua.getNeighborhood(), ua.getCity(), ua.getNumber(), ua.getZipCode(), ua.getCreateAt(), ua.getUpdateAt()
                );
            }
            proprietaryOutput = new CreateUserSystemOutput(
                    u.getId(), u.getUsername(), u.getName(), u.getEmail(), u.getUserType(), u.isActive(), userAddressOutput
            );
        }

        CreateRestaurantOutput out = new CreateRestaurantOutput(
                saved.getId(), saved.getName(), addressOutput, saved.getKitchenType(), saved.getStartOperationTime(), saved.getEndOperationTime(), proprietaryOutput
        );
        return Optional.of(out);
    }
}
