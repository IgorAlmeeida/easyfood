package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.infra.mappers.RestaurantItemMapper;
import br.com.imsa.easyfood.infra.model.RestaurantItemJpaEntity;
import br.com.imsa.easyfood.infra.repository.RestaurantItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantItemEntityRepositoryTest {

    @Mock
    private RestaurantItemRepository repository;
    @Mock
    private RestaurantItemMapper mapper;

    @InjectMocks
    private RestaurantItemEntityRepository sut;

    @Test
    @DisplayName("findById deve mapear corretamente")
    void findById_mapsCorrectly() {
        RestaurantItemJpaEntity entity = new RestaurantItemJpaEntity();
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        RestaurantItem domain = buildItem(1L);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<RestaurantItem> result = sut.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    @DisplayName("findAll deve mapear página")
    void findAll_mapsPage() {
        Pageable pageable = PageRequest.of(0, 2);
        RestaurantItemJpaEntity e1 = new RestaurantItemJpaEntity(); e1.setId(1L);
        RestaurantItemJpaEntity e2 = new RestaurantItemJpaEntity(); e2.setId(2L);
        Page<RestaurantItemJpaEntity> page = new PageImpl<>(List.of(e1, e2), pageable, 2);
        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(buildItem(1L));
        when(mapper.toDomain(e2)).thenReturn(buildItem(2L));

        Page<RestaurantItem> result = sut.findAll(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(2L, result.getContent().get(1).getId());
    }

    @Test
    @DisplayName("findByDescriptionContainingIgnoreCase deve mapear página")
    void findByDescription_mapsPage() {
        Pageable pageable = PageRequest.of(0, 5);
        RestaurantItemJpaEntity e1 = new RestaurantItemJpaEntity(); e1.setId(3L);
        Page<RestaurantItemJpaEntity> page = new PageImpl<>(List.of(e1), pageable, 1);
        when(repository.findByDescriptionContainingIgnoreCase("pizza", pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(buildItem(3L));

        Page<RestaurantItem> result = sut.findByDescriptionContainingIgnoreCase("pizza", pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(3L, result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("findByRestaurantId deve mapear página")
    void findByRestaurantId_mapsPage() {
        Pageable pageable = PageRequest.of(0, 5);
        RestaurantItemJpaEntity e1 = new RestaurantItemJpaEntity(); e1.setId(4L);
        Page<RestaurantItemJpaEntity> page = new PageImpl<>(List.of(e1), pageable, 1);
        when(repository.findByRestaurant_Id(9L, pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(buildItem(4L));

        Page<RestaurantItem> result = sut.findByRestaurantId(9L, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(4L, result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("findByRestaurantIdAndDescriptionContainingIgnoreCase deve mapear página")
    void findByRestaurantIdAndDescription_mapsPage() {
        Pageable pageable = PageRequest.of(0, 5);
        RestaurantItemJpaEntity e1 = new RestaurantItemJpaEntity(); e1.setId(6L);
        Page<RestaurantItemJpaEntity> page = new PageImpl<>(List.of(e1), pageable, 1);
        when(repository.findByRestaurant_IdAndDescriptionContainingIgnoreCase(7L, "sushi", pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(buildItem(6L));

        Page<RestaurantItem> result = sut.findByRestaurantIdAndDescriptionContainingIgnoreCase(7L, "sushi", pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(6L, result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("save deve converter, persistir e mapear")
    void save_convertsAndPersists() {
        RestaurantItem item = buildItem(null);
        RestaurantItemJpaEntity toSave = new RestaurantItemJpaEntity();
        when(mapper.toEntity(item)).thenReturn(toSave);
        RestaurantItemJpaEntity saved = new RestaurantItemJpaEntity(); saved.setId(8L);
        when(repository.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(buildItem(8L));

        RestaurantItem result = sut.save(item);
        assertEquals(8L, result.getId());
        verify(mapper).toEntity(item);
        verify(repository).save(toSave);
        verify(mapper).toDomain(saved);
    }

    @Test
    @DisplayName("delete deve chamar deleteById")
    void delete_callsRepository() {
        RestaurantItem item = buildItem(12L);
        sut.delete(item);
        verify(repository).deleteById(12L);
    }

    private RestaurantItem buildItem(Long id) {
        Restaurant restaurant = new Restaurant(
                1L,
                "Rest",
                null,
                KichenTypeEnum.ITALIAN,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                null
        );
        return new RestaurantItem(
                id,
                "Item",
                10.0,
                null,
                AvailabilityEnum.DELIVERY,
                restaurant
        );
    }
}
