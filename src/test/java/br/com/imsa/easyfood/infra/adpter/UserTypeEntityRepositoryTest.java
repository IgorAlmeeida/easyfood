package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.infra.mappers.UserTypeMapper;
import br.com.imsa.easyfood.infra.model.UserTypeJpaEntity;
import br.com.imsa.easyfood.infra.repository.UserTypeRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeEntityRepositoryTest {

    @Mock
    private UserTypeRepository repository;
    @Mock
    private UserTypeMapper mapper;

    @InjectMocks
    private UserTypeEntityRepository sut;

    @Test
    @DisplayName("findById deve mapear corretamente")
    void findById_mapsCorrectly() {
        UserTypeJpaEntity entity = new UserTypeJpaEntity();
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        UserType domain = new UserType(1L, "ADMIN");
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<UserType> result = sut.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    @DisplayName("findAll deve mapear página")
    void findAll_mapsPage() {
        Pageable pageable = PageRequest.of(0, 2);
        UserTypeJpaEntity e1 = new UserTypeJpaEntity(); e1.setId(1L);
        UserTypeJpaEntity e2 = new UserTypeJpaEntity(); e2.setId(2L);
        Page<UserTypeJpaEntity> page = new PageImpl<>(List.of(e1, e2), pageable, 2);
        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(new UserType(1L, "A"));
        when(mapper.toDomain(e2)).thenReturn(new UserType(2L, "B"));

        Page<UserType> result = sut.findAll(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase deve mapear página")
    void findByName_mapsPage() {
        Pageable pageable = PageRequest.of(0, 3);
        UserTypeJpaEntity e1 = new UserTypeJpaEntity(); e1.setId(10L);
        Page<UserTypeJpaEntity> page = new PageImpl<>(List.of(e1), pageable, 1);
        when(repository.findByNameContainingIgnoreCase("adm", pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(new UserType(10L, "ADMIN"));

        Page<UserType> result = sut.findByNameContainingIgnoreCase("adm", pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(10L, result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("save deve converter, persistir e mapear")
    void save_convertsAndPersists() {
        UserType domain = new UserType(null, "ADM");
        UserTypeJpaEntity toSave = new UserTypeJpaEntity();
        when(mapper.toEntity(domain)).thenReturn(toSave);
        UserTypeJpaEntity saved = new UserTypeJpaEntity(); saved.setId(9L);
        when(repository.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(new UserType(9L, "ADM"));

        UserType result = sut.save(domain);
        assertEquals(9L, result.getId());
        verify(mapper).toEntity(domain);
        verify(repository).save(toSave);
        verify(mapper).toDomain(saved);
    }

    @Test
    @DisplayName("delete deve chamar deleteById")
    void delete_callsRepository() {
        UserType domain = new UserType(15L, "ADM");
        sut.delete(domain);
        verify(repository).deleteById(15L);
    }
}
