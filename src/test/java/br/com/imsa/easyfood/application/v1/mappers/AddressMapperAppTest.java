package br.com.imsa.easyfood.application.v1.mappers;

import br.com.imsa.easyfood.application.v1.dto.requests.AddressRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.AddressResponse;
import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.dto.output.address.CreateAddressOutput;
import br.com.imsa.easyfood.domain.entity.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        AddressMapperAppImpl.class
})
class AddressMapperAppTest {

    @Autowired
    private AddressMapperApp mapper;

    @Test
    @DisplayName("toResponse(CreateAddressOutput) deve mapear todos os campos")
    void toResponse_fromCreateOutput_mapsAllFields() {
        CreateAddressOutput output = new CreateAddressOutput(1L, "Rua A", "Centro", "SP", "100", "00000000");

        AddressResponse resp = mapper.toResponse(output);

        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("Rua A", resp.getStreet());
        assertEquals("Centro", resp.getNeighborhood());
        assertEquals("SP", resp.getCity());
        assertEquals("100", resp.getNumber());
        assertEquals("00000000", resp.getZipCode());
    }

    @Test
    @DisplayName("toResponse(Address) deve mapear todos os campos do domínio")
    void toResponse_fromDomain_mapsAllFields() {
        Address domain = new Address(2L, "Rua B", "Bairro", "RJ", "200", "11111111");

        AddressResponse resp = mapper.toResponse(domain);

        assertNotNull(resp);
        assertEquals(2L, resp.getId());
        assertEquals("Rua B", resp.getStreet());
        assertEquals("Bairro", resp.getNeighborhood());
        assertEquals("RJ", resp.getCity());
        assertEquals("200", resp.getNumber());
        assertEquals("11111111", resp.getZipCode());
    }

    @Test
    @DisplayName("toCreateAddressInput(AddressRequest) deve mapear todos os campos")
    void toCreateAddressInput_mapsAllFields() {
        AddressRequest req = new AddressRequest("Rua C", "Vila", "BH", "300", "22222222");

        CreateAddressInput input = mapper.toCreateAddressInput(req);

        assertNotNull(input);
        assertEquals("Rua C", input.street());
        assertEquals("Vila", input.neighborhood());
        assertEquals("BH", input.city());
        assertEquals("300", input.number());
        assertEquals("22222222", input.zipCode());
    }

    @Test
    @DisplayName("toUpdateAddressInput(AddressRequest) deve mapear todos os campos e id nulo")
    void toUpdateAddressInput_mapsAllFields() {
        AddressRequest req = new AddressRequest("Rua D", "Jardim", "POA", "400", "33333333");

        UpdateAddressInput input = mapper.toUpdateAddressInput(req);

        assertNotNull(input);
        assertNull(input.id(), "Id deve ser nulo no mapeamento de update vindo do request");
        assertEquals("Rua D", input.street());
        assertEquals("Jardim", input.neighborhood());
        assertEquals("POA", input.city());
        assertEquals("400", input.number());
        assertEquals("33333333", input.zipCode());
    }

    @Nested
    @DisplayName("Cenários nulos e edge cases")
    class NullAndEdgeCases {
        @Test
        @DisplayName("toResponse(CreateAddressOutput) com null deve retornar null")
        void toResponse_fromCreateOutput_null() {
            assertNull(mapper.toResponse((CreateAddressOutput) null));
        }

        @Test
        @DisplayName("toResponse(Address) com null deve retornar null")
        void toResponse_fromDomain_null() {
            assertNull(mapper.toResponse((Address) null));
        }

        @Test
        @DisplayName("toCreateAddressInput(AddressRequest) com null deve retornar null")
        void toCreateAddressInput_null() {
            assertNull(mapper.toCreateAddressInput(null));
        }

        @Test
        @DisplayName("toUpdateAddressInput(AddressRequest) com null deve retornar null")
        void toUpdateAddressInput_null() {
            assertNull(mapper.toUpdateAddressInput(null));
        }
    }
}
