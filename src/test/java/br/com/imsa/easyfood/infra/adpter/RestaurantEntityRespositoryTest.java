package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.infra.mappers.RestaurantMapper;
import br.com.imsa.easyfood.infra.model.RestaurantJpaEntity;
import br.com.imsa.easyfood.infra.repository.RestaurantRepository;
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
class RestaurantEntityRespositoryTest {

    @Mock
    private RestaurantRepository repository;
    @Mock
    private RestaurantMapper mapper;

    @InjectMocks
    private RestaurantEntityRespository sut;

    @Test
    @DisplayName("findById deve mapear corretamente")
    void findById_mapsCorrectly() {
        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        Restaurant domain = buildDomain(1L);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Restaurant> result = sut.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    @DisplayName("findAll deve mapear página de entidades")
    void findAll_mapsPage() {
        Pageable pageable = PageRequest.of(0, 2);
        RestaurantJpaEntity e1 = new RestaurantJpaEntity(); e1.setId(1L);
        RestaurantJpaEntity e2 = new RestaurantJpaEntity(); e2.setId(2L);
        Page<RestaurantJpaEntity> page = new PageImpl<>(List.of(e1, e2), pageable, 2);
        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(buildDomain(1L));
        when(mapper.toDomain(e2)).thenReturn(buildDomain(2L));

        Page<Restaurant> result = sut.findAll(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(2L, result.getContent().get(1).getId());
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase deve mapear página")
    void findByNameContainingIgnoreCase_mapsPage() {
        Pageable pageable = PageRequest.of(1, 3);
        RestaurantJpaEntity e1 = new RestaurantJpaEntity(); e1.setId(10L);
        Page<RestaurantJpaEntity> page = new PageImpl<>(List.of(e1), pageable, 1);
        when(repository.findByNameContainingIgnoreCase("abc", pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(buildDomain(10L));

        Page<Restaurant> result = sut.findByNameContainingIgnoreCase("abc", pageable);
        assertEquals(1, result.getContent().size());
        assertEquals(10L, result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("save deve converter, persistir e mapear")
    void save_convertsAndPersists() {
        Restaurant domain = buildDomain(null);
        RestaurantJpaEntity toSave = new RestaurantJpaEntity();
        when(mapper.toEntity(domain)).thenReturn(toSave);
        RestaurantJpaEntity saved = new RestaurantJpaEntity(); saved.setId(7L);
        when(repository.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(buildDomain(7L));

        Restaurant result = sut.save(domain);
        assertEquals(7L, result.getId());
        verify(mapper).toEntity(domain);
        verify(repository).save(toSave);
        verify(mapper).toDomain(saved);
    }

    @Test
    @DisplayName("delete deve chamar deleteById do repositório")
    void delete_callsRepository() {
        Restaurant domain = buildDomain(55L);
        sut.delete(domain);
        verify(repository).deleteById(55L);
    }

    private Restaurant buildDomain(Long id) {
        return new Restaurant(
                id,
                "Rest",
                null,
                KichenTypeEnum.ITALIAN,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                null
        );
    }
}
