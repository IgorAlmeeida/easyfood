package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.infra.mappers.UserSystemMapper;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import br.com.imsa.easyfood.infra.model.UserTypeJpaEntity;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSystemEntityRepositoryTest {

    @Mock
    private UserSystemRepository repository;
    @Mock
    private UserTypeRepository userTypeRepository;
    @Mock
    private UserSystemMapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserSystemEntityRepository sut;

    private Address validAddress() {
        return new Address("Street A", "Centro", "Sao Paulo", "123", "01000-000");
    }

    @Test
    @DisplayName("findById deve mapear corretamente")
    void findById_mapsCorrectly() {
        UserSystemJpaEntity entity = new UserSystemJpaEntity();
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        UserSystem domain = new UserSystem(1L, "Name", "email@t.com", "user", "secret1", true, new UserType(1L, "ADM"), validAddress());
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<UserSystem> result = sut.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    @DisplayName("findByUsername deve mapear corretamente")
    void findByUsername_mapsCorrectly() {
        UserSystemJpaEntity entity = new UserSystemJpaEntity();
        when(repository.findByUsername("u")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(new UserSystem(2L, "N", "e@x.com", "u", "secret1", true, new UserType(1L, "ADM"), validAddress()));

        Optional<UserSystem> result = sut.findByUsername("u");
        assertTrue(result.isPresent());
        assertEquals("u", result.get().getUsername());
    }

    @Test
    @DisplayName("findByEmail deve mapear corretamente")
    void findByEmail_mapsCorrectly() {
        UserSystemJpaEntity entity = new UserSystemJpaEntity();
        when(repository.findUserSystemByEmail("e@x.com")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(new UserSystem(3L, "N", "e@x.com", "u", "secret1", true, new UserType(1L, "ADM"), validAddress()));

        Optional<UserSystem> result = sut.findByEmail("e@x.com");
        assertTrue(result.isPresent());
        assertEquals("e@x.com", result.get().getEmail());
    }

    @Test
    @DisplayName("findAll deve mapear página")
    void findAll_mapsPage() {
        Pageable pageable = PageRequest.of(0, 2);
        UserSystemJpaEntity e1 = new UserSystemJpaEntity();
        UserSystemJpaEntity e2 = new UserSystemJpaEntity();
        Page<UserSystemJpaEntity> page = new PageImpl<>(List.of(e1, e2), pageable, 2);
        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(new UserSystem(1L, "N1", "e1@x.com", "u1", "secret1", true, new UserType(1L, "ADM"), validAddress()));
        when(mapper.toDomain(e2)).thenReturn(new UserSystem(2L, "N2", "e2@x.com", "u2", "secret1", true, new UserType(2L, "USER"), validAddress()));

        Page<UserSystem> result = sut.findAll(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase deve mapear página")
    void findByName_mapsPage() {
        Pageable pageable = PageRequest.of(0, 3);
        UserSystemJpaEntity e1 = new UserSystemJpaEntity();
        Page<UserSystemJpaEntity> page = new PageImpl<>(List.of(e1), pageable, 1);
        when(repository.findUserSystemByNameContainingIgnoreCase("jo", pageable)).thenReturn(page);
        when(mapper.toDomain(e1)).thenReturn(new UserSystem(10L, "Joao", "joao@x.com", "u", "secret1", true, new UserType(1L, "ADM"), validAddress()));

        Page<UserSystem> result = sut.findByNameContainingIgnoreCase("jo", pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("Joao", result.getContent().get(0).getName());
    }

    @Test
    @DisplayName("save deve preencher UserType a partir do repositório (encontrado)")
    void save_setsUserType_whenFound() {
        UserSystem domain = new UserSystem(null, "N", "e@x.com", "u", "secret1", true, new UserType(5L, "ADM"), validAddress());
        UserSystemJpaEntity toSave = new UserSystemJpaEntity();
        // ensure mapper returns an entity with a UserType id so adapter can query repository without NPE
        UserTypeJpaEntity userTypeInEntity = new UserTypeJpaEntity(); userTypeInEntity.setId(5L);
        toSave.setUserType(userTypeInEntity);
        UserTypeJpaEntity typeEntity = new UserTypeJpaEntity(); typeEntity.setId(5L);
        when(mapper.toEntity(eq(domain), any(PasswordEncoder.class))).thenReturn(toSave);
        when(userTypeRepository.findById(5L)).thenReturn(Optional.of(typeEntity));
        when(repository.save(toSave)).thenReturn(toSave);
        when(mapper.toDomain(toSave)).thenReturn(new UserSystem(20L, "N", "e@x.com", "u", "secret1", true, new UserType(5L, "ADM"), validAddress()));

        UserSystem result = sut.save(domain);
        assertEquals(20L, result.getId());
        verify(userTypeRepository).findById(5L);
        verify(repository).save(toSave);
    }

    @Test
    @DisplayName("save deve setar UserType como null quando não encontrado")
    void save_setsUserTypeNull_whenNotFound() {
        UserSystem domain = new UserSystem(null, "N", "e50@x.com", "u", "secret1", true, new UserType(50L, "ADM"), validAddress());
        UserSystemJpaEntity toSave = new UserSystemJpaEntity();
        // ensure mapper returns an entity with a UserType id so adapter can attempt lookup and then nullify
        UserTypeJpaEntity userTypeInEntity = new UserTypeJpaEntity(); userTypeInEntity.setId(50L);
        toSave.setUserType(userTypeInEntity);
        when(mapper.toEntity(eq(domain), any(PasswordEncoder.class))).thenReturn(toSave);
        when(userTypeRepository.findById(50L)).thenReturn(Optional.empty());
        when(repository.save(toSave)).thenReturn(toSave);
        // domain object returned by mapper must satisfy domain validations, so provide a valid non-null userType
        when(mapper.toDomain(toSave)).thenReturn(new UserSystem(21L, "N", "e50@x.com", "u", "secret1", true, new UserType(1L, "USER"), validAddress()));

        UserSystem result = sut.save(domain);
        assertEquals(21L, result.getId());
        assertNull(toSave.getUserType());
        verify(userTypeRepository).findById(50L);
    }

    @Test
    @DisplayName("delete deve chamar deleteById")
    void delete_callsRepository() {
        UserSystem domain = new UserSystem(99L, "N", "e@x.com", "u", "secret1", true, new UserType(1L, "ADM"), validAddress());
        sut.delete(domain);
        verify(repository).deleteById(99L);
    }
}
