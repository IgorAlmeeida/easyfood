package br.com.imsa.easyfood.domain.usercase.restaurant;

import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.restaurant.CreateRestaurantInput;
import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.domain.dto.output.restaurant.CreateRestaurantOutput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.CreateUserSystemOutput;
import br.com.imsa.easyfood.domain.dto.output.usertype.CreateUserTypeOutput;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.domain.gateway.RestaurantGateway;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;
    @Mock
    private AddressGateway addressGateway;
    @Mock
    private UserSystemGateway userSystemGateway;

    @InjectMocks
    private CreateRestaurantUseCase useCase;

    @Test
    @DisplayName("Deve retornar Optional.empty quando input é null")
    void nullInput() {
        assertTrue(useCase.execute(null).isEmpty());
    }

    @Test
    @DisplayName("Happy path com endereço e proprietário válidos")
    void happyPathWithAddressAndProprietary() {
        LocalDateTime start = LocalDateTime.of(2026,1,1,9,0);
        LocalDateTime end = start.plusHours(8);
        CreateAddressInput addrIn = new CreateAddressInput("Rua A", "Centro", "SP", "10", "00000-000");
        CreateRestaurantInput input = new CreateRestaurantInput("Rest X", addrIn, KichenTypeEnum.ITALIAN, start, end, 99L);

        // mock usuário/proprietário existente
        Address userAddr = new Address(1L, "R", "B", "C", "1", "11111-111");
        UserSystem user = new UserSystem(99L, "Nome", "a@b.com", "user", "secret1", true, new UserType(1L, "ADMIN"), userAddr);
        when(userSystemGateway.findById(99L)).thenReturn(Optional.of(user));

        // mock save restaurante retornando entidade com id e address com id
        Address savedAddress = new Address(5L, addrIn.street(), addrIn.neighborhood(), addrIn.city(), addrIn.number(), addrIn.zipCode());
        Restaurant saved = new Restaurant(10L, input.name(), savedAddress, input.kitchenType(), start, end, user);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(saved);

        Optional<CreateRestaurantOutput> out = useCase.execute(input);
        assertTrue(out.isPresent());
        CreateRestaurantOutput o = out.get();
        assertAll(
                () -> assertEquals(10L, o.id()),
                () -> assertEquals("Rest X", o.name()),
                () -> assertNotNull(o.address()),
                () -> assertEquals(KichenTypeEnum.ITALIAN, o.kitchenType()),
                () -> assertEquals(start, o.startOperationTime()),
                () -> assertEquals(end, o.endOperationTime()),
                () -> assertNotNull(o.proprietary())
        );
        CreateAddressOutput addrOut = o.address();
        assertAll(
                () -> assertEquals(5L, addrOut.id()),
                () -> assertEquals("Rua A", addrOut.street())
        );
        CreateUserSystemOutput uo = o.proprietary();
        assertAll(
                () -> assertEquals(99L, uo.id()),
                () -> assertEquals("user", uo.username()),
                () -> assertEquals("Nome", uo.name()),
                () -> assertEquals("a@b.com", uo.email()),
                () -> assertNotNull(uo.userType()),
                () -> assertTrue(uo.active())
        );
        CreateUserTypeOutput uto = uo.userType();
        assertAll(
                () -> assertEquals(1L, uto.id()),
                () -> assertEquals("ADMIN", uto.name())
        );
    }

    @Test
    @DisplayName("Deve lidar com address = null e proprietaryId = null")
    void nullAddressAndProprietary() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(4);
        CreateRestaurantInput input = new CreateRestaurantInput("R", null, KichenTypeEnum.BRAZILIAN, start, end, null);
        Restaurant saved = new Restaurant(7L, "R", null, KichenTypeEnum.BRAZILIAN, start, end, null);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(saved);
        Optional<CreateRestaurantOutput> out = useCase.execute(input);
        assertTrue(out.isPresent());
        CreateRestaurantOutput o = out.get();
        assertNull(o.address());
        assertNull(o.proprietary());
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando save retorna null")
    void saveReturnsNull() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(4);
        CreateRestaurantInput input = new CreateRestaurantInput("R", null, KichenTypeEnum.BRAZILIAN, start, end, null);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(null);
        assertTrue(useCase.execute(input).isEmpty());
    }
}
