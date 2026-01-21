package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.infra.mappers.AddressMapper;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import br.com.imsa.easyfood.infra.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressEntityRepositoryTest {

    @Mock
    private AddressRepository repository;
    @Mock
    private AddressMapper mapper;

    @InjectMocks
    private AddressEntityRepository sut;

    private Address domain;
    private AddressJpaEntity entity;

    @BeforeEach
    void setUp() {
        domain = new Address(1L, "Street", "Neighborhood", "City", "10A", "00000-000");
        entity = new AddressJpaEntity();
        entity.setId(1L);
        entity.setStreet("Street");
        entity.setNeighborhood("Neighborhood");
        entity.setCity("City");
        entity.setNumber("10A");
        entity.setZipCode("00000-000");
    }

    @Test
    @DisplayName("findById deve mapear Optional corretamente")
    void findById_shouldMapOptional() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Address> result = sut.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
        verify(repository).findById(1L);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("findById deve retornar Optional.empty quando não encontrar")
    void findById_shouldReturnEmptyWhenNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        Optional<Address> result = sut.findById(2L);

        assertTrue(result.isEmpty());
        verify(repository).findById(2L);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("save deve converter domain para entity, persistir e mapear de volta")
    void save_shouldConvertAndPersist() {
        when(mapper.toEntity(domain)).thenReturn(entity);
        AddressJpaEntity saved = new AddressJpaEntity();
        saved.setId(99L);
        when(repository.save(entity)).thenReturn(saved);
        Address mappedBack = new Address(99L, "Street", "Neighborhood", "City", "10A", "00000-000");
        when(mapper.toDomain(saved)).thenReturn(mappedBack);

        Address result = sut.save(domain);

        assertEquals(99L, result.getId());
        verify(mapper).toEntity(domain);
        verify(repository).save(entity);
        verify(mapper).toDomain(saved);
    }

    @Test
    @DisplayName("update deve setar o id antes de salvar e mapear de volta")
    void update_shouldSetIdBeforeSave() {
        when(mapper.toEntity(domain)).thenReturn(new AddressJpaEntity());
        ArgumentCaptor<AddressJpaEntity> captor = ArgumentCaptor.forClass(AddressJpaEntity.class);
        AddressJpaEntity saved = new AddressJpaEntity();
        saved.setId(5L);
        when(repository.save(any(AddressJpaEntity.class))).thenReturn(saved);
        Address mappedBack = new Address(5L, "Street", "Neighborhood", "City", "10A", "00000-000");
        when(mapper.toDomain(saved)).thenReturn(mappedBack);

        Address result = sut.update(5L, domain);

        assertEquals(5L, result.getId());
        verify(repository).save(captor.capture());
        assertEquals(5L, captor.getValue().getId());
    }

    @Test
    @DisplayName("save com input nulo deve retornar null e interagir com dependências")
    void save_nullInput_shouldReturnNull() {
        when(mapper.toEntity(null)).thenReturn(null);
        Address result = sut.save(null);
        assertNull(result);
        verify(mapper).toEntity(null);
        verify(repository).save(null);
        verify(mapper).toDomain(null);
    }
}
