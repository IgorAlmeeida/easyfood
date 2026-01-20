package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.restaurant.UpdateRestaurantInput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.domain.exception.NegocioException;
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
        if (opt.isEmpty()){
            return Optional.empty();
        }

        Restaurant current = opt.get();

        Address finalAddress = current.getAddress();
        UpdateAddressInput addrInput = input.address();
        if (addrInput != null) {

            Address mergedAddr = new Address(
                    addrInput.id(),
                    addrInput.street(),
                    addrInput.neighborhood(),
                    addrInput.city(),
                    addrInput.number(),
                    addrInput.zipCode());

            addressGateway.update(addrInput.id(), mergedAddr);
        }

        UserSystem proprietary = userSystemGateway.findById(input.proprietaryId())
                .orElseThrow(() -> new NegocioException("Usuário não cadastrado no sistema"));

        Restaurant merged = new Restaurant(
                current.getId(),
                input.name(),
                finalAddress,
                input.kitchenType(),
                input.startOperationTime(),
                input.endOperationTime(),
                proprietary
        );

        Restaurant saved = restaurantGateway.save(merged);
        return Optional.ofNullable(saved);
    }
}
