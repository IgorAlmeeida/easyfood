package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private UserSystemMapper userSystemMapper;

    @InjectMocks
    private RestaurantMapper mapper;

    private Address address;
    private AddressJpaEntity addressEntity;
    private UserSystem userSystem;
    private UserSystemJpaEntity userSystemEntity;

    @BeforeEach
    void setup() {
        address = new Address(1L, "Rua X", "Centro", "SP", "10", "00000-000");
        addressEntity = new AddressJpaEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Rua X");
        addressEntity.setNeighborhood("Centro");
        addressEntity.setCity("SP");
        addressEntity.setNumber("10");
        addressEntity.setZipCode("00000-000");

        userSystem = new UserSystem(1L, "Nome", "email@ex.com", "user", "123456", true, new UserType(1L, "ADMIN"), address);
        userSystemEntity = new UserSystemJpaEntity();
        userSystemEntity.setId(1L);
        userSystemEntity.setName("Nome");
        userSystemEntity.setEmail("email@ex.com");
        userSystemEntity.setUsername("user");
        userSystemEntity.setPassword("hash");
        userSystemEntity.setActive(true);
        userSystemEntity.setAddressJpaEntity(addressEntity);
    }

    @Test
    @DisplayName("toEntity deve retornar null quando domain é null")
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("toEntity deve mapear todos os campos incluindo aninhados")
    void toEntityAllFields() {
        when(addressMapper.toEntity(any())).thenReturn(addressEntity);
        when(userSystemMapper.toEntity(any(UserSystem.class), any())).thenReturn(userSystemEntity);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(8);
        Restaurant domain = new Restaurant(10L, "Rest X", address, KichenTypeEnum.BRAZILIAN, start, end, userSystem);

        RestaurantJpaEntity entity = mapper.toEntity(domain);
        assertAll(
                () -> assertEquals(10L, entity.getId()),
                () -> assertEquals("Rest X", entity.getName()),
                () -> assertEquals(KichenTypeEnum.BRAZILIAN, entity.getKitchenType()),
                () -> assertEquals(start, entity.getStartOperationTime()),
                () -> assertEquals(end, entity.getEndOperationTime()),
                () -> assertNotNull(entity.getAddress()),
                () -> assertNotNull(entity.getProprietary())
        );
    }

    @Test
    @DisplayName("toEntity deve lidar com address e proprietary null")
    void toEntityWithNullNested() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(8);
        Restaurant domain = new Restaurant(10L, "Rest Y", null, KichenTypeEnum.ITALIAN, start, end, null);
        RestaurantJpaEntity entity = mapper.toEntity(domain);
        assertAll(
                () -> assertNull(entity.getAddress()),
                () -> assertNull(entity.getProprietary())
        );
    }

    @Test
    @DisplayName("toDomain deve retornar null quando entity é null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toDomain deve mapear todos os campos incluindo aninhados")
    void toDomainAllFields() {
        when(userSystemMapper.toDomain(any())).thenReturn(userSystem);
        when(addressMapper.toDomain(any())).thenReturn(address);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(8);
        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        entity.setId(77L);
        entity.setName("Risto");
        entity.setKitchenType(KichenTypeEnum.ITALIAN);
        entity.setStartOperationTime(start);
        entity.setEndOperationTime(end);
        entity.setAddress(addressEntity);
        entity.setProprietary(userSystemEntity);

        Restaurant domain = mapper.toDomain(entity);
        assertAll(
                () -> assertEquals(77L, domain.getId()),
                () -> assertEquals("Risto", domain.getName()),
                () -> assertEquals(KichenTypeEnum.ITALIAN, domain.getKitchenType()),
                () -> assertEquals(start, domain.getStartOperationTime()),
                () -> assertEquals(end, domain.getEndOperationTime()),
                () -> assertNotNull(domain.getAddress()),
                () -> assertNotNull(domain.getProprietary())
        );
    }

    @Test
    @DisplayName("toDomain deve lidar com nested null")
    void toDomainWithNullNested() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(8);
        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        entity.setId(1L);
        entity.setName("R");
        entity.setKitchenType(KichenTypeEnum.BRAZILIAN);
        entity.setStartOperationTime(start);
        entity.setEndOperationTime(end);
        entity.setAddress(null);
        entity.setProprietary(null);

        Restaurant domain = mapper.toDomain(entity);
        assertAll(
                () -> assertNull(domain.getAddress()),
                () -> assertNull(domain.getProprietary())
        );
    }
}
