package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSystemMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private UserTypeMapper userTypeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserSystemMapper mapper;

    private UserSystem buildDomain(String password) {
        Address address = new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000");
        UserType userType = new UserType(1L, "ADMIN");
        return new UserSystem(1L, "Nome", "email@ex.com", "user", password, true, userType, address);
    }

    private UserSystemJpaEntity buildEntity() {
        UserSystemJpaEntity e = new UserSystemJpaEntity();
        e.setId(2L);
        e.setName("Nome");
        e.setEmail("email@ex.com");
        e.setUsername("user");
        e.setPassword("$2a$encoded");
        e.setActive(true);
        e.setAddressJpaEntity(new AddressJpaEntity());
        return e;
    }

    @Nested
    class ToEntityTests {
        @Test
        @DisplayName("toEntity deve retornar null quando domain é null")
        void toEntityNull() {
            assertNull(mapper.toEntity(null, passwordEncoder));
        }

        @Test
        @DisplayName("toEntity deve manter hash bcrypt sem recodificar")
        void toEntityKeepBcrypt() {
            when(userTypeMapper.toEntity(any())).thenReturn(null);
            when(addressMapper.toEntity(any())).thenReturn(new AddressJpaEntity());
            UserSystem domain = buildDomain("$2b$alreadyHashed");
            UserSystemJpaEntity entity = mapper.toEntity(domain, passwordEncoder);
            assertEquals("$2b$alreadyHashed", entity.getPassword());
        }

        @Test
        @DisplayName("toEntity deve codificar senha quando plain e encoder presente")
        void toEntityEncodePlainWithEncoder() {
            when(userTypeMapper.toEntity(any())).thenReturn(null);
            when(addressMapper.toEntity(any())).thenReturn(new AddressJpaEntity());
            when(passwordEncoder.encode("plain123")).thenReturn("ENCODED");
            UserSystem domain = buildDomain("plain123");
            UserSystemJpaEntity entity = mapper.toEntity(domain, passwordEncoder);
            assertEquals("ENCODED", entity.getPassword());
        }

        @Test
        @DisplayName("toEntity não deve alterar senha quando encoder for null e senha não-bcrypt")
        void toEntityNoEncoderPlain() {
            when(userTypeMapper.toEntity(any())).thenReturn(null);
            when(addressMapper.toEntity(any())).thenReturn(new AddressJpaEntity());
            UserSystem domain = buildDomain("plain123");
            UserSystemJpaEntity entity = mapper.toEntity(domain, null);
            assertNull(entity.getPassword(), "Sem encoder a senha não deve ser setada");
        }

        @Test
        @DisplayName("toEntity deve ignorar senha null ou vazia")
        void toEntityIgnoreNullOrBlank() {
            when(userTypeMapper.toEntity(any())).thenReturn(null);
            when(addressMapper.toEntity(any())).thenReturn(new AddressJpaEntity());

            // Usar mock para contornar validações do domínio e simular senha null
            UserSystem domainNullPwd = org.mockito.Mockito.mock(UserSystem.class);
            org.mockito.Mockito.when(domainNullPwd.getPassword()).thenReturn(null);
            UserSystemJpaEntity entity = mapper.toEntity(domainNullPwd, passwordEncoder);
            assertNull(entity.getPassword());

            // Simular senha vazia
            UserSystem domainBlankPwd = org.mockito.Mockito.mock(UserSystem.class);
            org.mockito.Mockito.when(domainBlankPwd.getPassword()).thenReturn("");
            entity = mapper.toEntity(domainBlankPwd, passwordEncoder);
            assertNull(entity.getPassword());
        }
    }

    @Nested
    class ToDomainTests {
        @Test
        @DisplayName("toDomain deve retornar null quando entity é null")
        void toDomainNull() {
            assertNull(mapper.toDomain(null));
        }

        @Test
        @DisplayName("toDomain deve mapear campos básicos e nested usando mappers")
        void toDomainAllFields() {
            when(userTypeMapper.toDomain(any())).thenReturn(new UserType(1L, "ADMIN"));
            when(addressMapper.toDomain(any())).thenReturn(new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000"));
            UserSystemJpaEntity entity = buildEntity();
            UserSystem domain = mapper.toDomain(entity);
            assertAll(
                    () -> assertEquals(2L, domain.getId()),
                    () -> assertEquals("Nome", domain.getName()),
                    () -> assertEquals("email@ex.com", domain.getEmail()),
                    () -> assertEquals("user", domain.getUsername()),
                    () -> assertEquals("$2a$encoded", domain.getPassword()),
                    () -> assertTrue(domain.isActive()),
                    () -> assertNotNull(domain.getUserType()),
                    () -> assertNotNull(domain.getAddress())
            );
        }
    }

    @Test
    @DisplayName("isBCryptHash deve validar prefixos suportados")
    void isBCryptHash() {
        assertTrue(mapper.isBCryptHash("$2a$something"));
        assertTrue(mapper.isBCryptHash("$2b$something"));
        assertTrue(mapper.isBCryptHash("$2y$something"));
        assertFalse(mapper.isBCryptHash("plain"));
        assertFalse(mapper.isBCryptHash(null));
    }
}
