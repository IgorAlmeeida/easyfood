package br.com.imsa.easyfood.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KichenTypeEnumTest {

    @Test
    @DisplayName("getByAcronym deve localizar ignorando case e retornar null quando não encontrar")
    void testGetByAcronym() {
        assertEquals(KichenTypeEnum.ITALIAN, KichenTypeEnum.getByAcronym("i"));
        assertEquals(KichenTypeEnum.BRAZILIAN, KichenTypeEnum.getByAcronym("B"));
        assertNull(KichenTypeEnum.getByAcronym("X"));
        assertNull(KichenTypeEnum.getByAcronym(null));
    }

    @Test
    @DisplayName("getByDescription deve localizar ignorando case e retornar null quando não encontrar")
    void testGetByDescription() {
        assertEquals(KichenTypeEnum.ITALIAN, KichenTypeEnum.getByDescription("italiana"));
        assertEquals(KichenTypeEnum.BRAZILIAN, KichenTypeEnum.getByDescription("BRASILEIRA"));
        assertNull(KichenTypeEnum.getByDescription("N/A"));
        assertNull(KichenTypeEnum.getByDescription(null));
    }
}
