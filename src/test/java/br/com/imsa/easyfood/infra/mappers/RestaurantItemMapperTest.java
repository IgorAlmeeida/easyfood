package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import br.com.imsa.easyfood.infra.model.RestaurantItemJpaEntity;
import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantItemMapperTest {

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantItemMapper mapper;

    @Test
    @DisplayName("toEntity deve retornar null quando domain é null")
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("toEntity deve mapear todos os campos incluindo Restaurant")
    void toEntityAllFields() {
        Restaurant restaurant = new Restaurant("R", null, br.com.imsa.easyfood.domain.enums.KichenTypeEnum.ITALIAN, java.time.LocalDateTime.now(), java.time.LocalDateTime.now().plusHours(1), null);
        RestaurantJpaEntity restaurantEntity = new RestaurantJpaEntity();
        when(restaurantMapper.toEntity(any())).thenReturn(restaurantEntity);

        RestaurantItem domain = new RestaurantItem(1L, "Item", 10.0, "img", AvailabilityEnum.DELIVERY, restaurant);
        RestaurantItemJpaEntity entity = mapper.toEntity(domain);
        assertAll(
                () -> assertEquals(1L, entity.getId()),
                () -> assertEquals("Item", entity.getDescription()),
                () -> assertEquals(10.0, entity.getPrice()),
                () -> assertEquals("img", entity.getImage()),
                () -> assertEquals(AvailabilityEnum.DELIVERY, entity.getAvailability()),
                () -> assertNotNull(entity.getRestaurant())
        );
    }

    @Test
    @DisplayName("toEntity deve lidar com Restaurant null")
    void toEntityRestaurantNull() {
        RestaurantItem domain = new RestaurantItem(2L, "Item2", 12.0, null, AvailabilityEnum.LOCAL, null);
        RestaurantItemJpaEntity entity = mapper.toEntity(domain);
        assertNull(entity.getRestaurant());
    }

    @Test
    @DisplayName("toDomain deve retornar null quando entity é null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toDomain deve mapear todos os campos incluindo Restaurant")
    void toDomainAllFields() {
        RestaurantJpaEntity restaurantEntity = new RestaurantJpaEntity();
        when(restaurantMapper.toDomain(any())).thenReturn(new Restaurant("R", null, br.com.imsa.easyfood.domain.enums.KichenTypeEnum.ITALIAN, java.time.LocalDateTime.now(), java.time.LocalDateTime.now().plusHours(1), null));

        RestaurantItemJpaEntity entity = new RestaurantItemJpaEntity();
        entity.setId(3L);
        entity.setDescription("Desc");
        entity.setPrice(5.0);
        entity.setImage("img2");
        entity.setAvailability(AvailabilityEnum.LOCAL);
        entity.setRestaurant(restaurantEntity);

        RestaurantItem domain = mapper.toDomain(entity);
        assertAll(
                () -> assertEquals(3L, domain.getId()),
                () -> assertEquals("Desc", domain.getDescription()),
                () -> assertEquals(5.0, domain.getPrice()),
                () -> assertEquals("img2", domain.getImage()),
                () -> assertEquals(AvailabilityEnum.LOCAL, domain.getAvailability()),
                () -> assertNotNull(domain.getRestaurant())
        );
    }

    @Test
    @DisplayName("toDomain deve lidar com Restaurant null")
    void toDomainRestaurantNull() {
        RestaurantItemJpaEntity entity = new RestaurantItemJpaEntity();
        entity.setId(4L);
        entity.setDescription("D");
        entity.setPrice(9.0);
        entity.setImage(null);
        entity.setAvailability(AvailabilityEnum.DELIVERY);
        entity.setRestaurant(null);

        RestaurantItem domain = mapper.toDomain(entity);
        assertNull(domain.getRestaurant());
    }
}
