package br.com.imsa.easyfood.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityEnumTest {

    @Test
    @DisplayName("getByAcronym deve localizar ignorando case e retornar null quando não encontrar")
    void testGetByAcronym() {
        assertEquals(AvailabilityEnum.DELIVERY, AvailabilityEnum.getByAcronym("d"));
        assertEquals(AvailabilityEnum.LOCAL, AvailabilityEnum.getByAcronym("L"));
        assertNull(AvailabilityEnum.getByAcronym("X"));
        assertNull(AvailabilityEnum.getByAcronym(null));
    }

    @Test
    @DisplayName("getByDescription deve localizar ignorando case e retornar null quando não encontrar")
    void testGetByDescription() {
        assertEquals(AvailabilityEnum.DELIVERY, AvailabilityEnum.getByDescription("delivery"));
        assertEquals(AvailabilityEnum.LOCAL, AvailabilityEnum.getByDescription("LOCAL"));
        assertNull(AvailabilityEnum.getByDescription("N/A"));
        assertNull(AvailabilityEnum.getByDescription(null));
    }
}
