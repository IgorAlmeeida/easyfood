package br.com.imsa.easyfood.infra.mappers;

import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressMapperTest {

    private final AddressMapper mapper = new AddressMapper();

    @Test
    @DisplayName("toEntity deve retornar null quando Address é null")
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("toEntity deve mapear todos os campos")
    void toEntityAllFields() {
        Address domain = new Address(10L, "Rua A", "Centro", "SP", "123", "00000-000");
        AddressJpaEntity entity = mapper.toEntity(domain);
        assertAll(
                () -> assertEquals(10L, entity.getId()),
                () -> assertEquals("Rua A", entity.getStreet()),
                () -> assertEquals("Centro", entity.getNeighborhood()),
                () -> assertEquals("SP", entity.getCity()),
                () -> assertEquals("123", entity.getNumber()),
                () -> assertEquals("00000-000", entity.getZipCode())
        );
    }

    @Test
    @DisplayName("toDomain deve retornar null quando entity é null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toDomain deve mapear todos os campos")
    void toDomainAllFields() {
        AddressJpaEntity entity = new AddressJpaEntity();
        entity.setId(11L);
        entity.setStreet("Rua B");
        entity.setNeighborhood("Bairro");
        entity.setCity("RJ");
        entity.setNumber("999");
        entity.setZipCode("11111-111");

        Address domain = mapper.toDomain(entity);
        assertAll(
                () -> assertEquals(11L, domain.getId()),
                () -> assertEquals("Rua B", domain.getStreet()),
                () -> assertEquals("Bairro", domain.getNeighborhood()),
                () -> assertEquals("RJ", domain.getCity()),
                () -> assertEquals("999", domain.getNumber()),
                () -> assertEquals("11111-111", domain.getZipCode())
        );
    }

    @Test
    @DisplayName("update deve fazer nada quando target ou request são null")
    void updateNoOpWhenNulls() {
        mapper.update(null, null); // não deve lançar
        AddressJpaEntity target = new AddressJpaEntity();
        mapper.update(target, null); // não deve lançar
        assertAll(
                () -> assertNull(target.getStreet()),
                () -> assertNull(target.getNeighborhood()),
                () -> assertNull(target.getCity()),
                () -> assertNull(target.getNumber()),
                () -> assertNull(target.getZipCode())
        );
    }

    @Test
    @DisplayName("update deve atualizar todos os campos")
    void updateAllFields() {
        AddressJpaEntity target = new AddressJpaEntity();
        target.setStreet("old");
        target.setNeighborhood("old");
        target.setCity("old");
        target.setNumber("old");
        target.setZipCode("old");

        Address request = new Address(1L, "Nova Rua", "Novo Bairro", "Nova Cidade", "321", "22222-222");
        mapper.update(target, request);

        assertAll(
                () -> assertEquals("Nova Rua", target.getStreet()),
                () -> assertEquals("Novo Bairro", target.getNeighborhood()),
                () -> assertEquals("Nova Cidade", target.getCity()),
                () -> assertEquals("321", target.getNumber()),
                () -> assertEquals("22222-222", target.getZipCode())
        );
    }
}
