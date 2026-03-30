package com.medicamentos.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MedicamentoTest {

    @Test
    void testMedicamentoCreation() {
        Medicamento medicamento = new Medicamento();
        medicamento.setId(1L);
        medicamento.setNombre("Acetaminofén 500mg");
        medicamento.setDescripcion("Analgésico y antipirético");
        medicamento.setEsNoPos(false);
        medicamento.setCreatedAt(LocalDateTime.now());

        assertEquals(1L, medicamento.getId());
        assertEquals("Acetaminofén 500mg", medicamento.getNombre());
        assertEquals("Analgésico y antipirético", medicamento.getDescripcion());
        assertFalse(medicamento.getEsNoPos());
        assertNotNull(medicamento.getCreatedAt());
    }

    @Test
    void testMedicamentoNoPOS() {
        Medicamento medicamento = new Medicamento();
        medicamento.setId(2L);
        medicamento.setNombre("Adalimumab 40mg");
        medicamento.setDescripcion("Medicamento biológico");
        medicamento.setEsNoPos(true);

        assertTrue(medicamento.getEsNoPos());
    }

    @Test
    void testMedicamentoAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Medicamento medicamento = new Medicamento(
            1L,
            "Acetaminofén 500mg",
            "Analgésico",
            false,
            now
        );

        assertEquals(1L, medicamento.getId());
        assertEquals("Acetaminofén 500mg", medicamento.getNombre());
        assertEquals("Analgésico", medicamento.getDescripcion());
        assertFalse(medicamento.getEsNoPos());
        assertEquals(now, medicamento.getCreatedAt());
    }

    @Test
    void testMedicamentoNoArgsConstructor() {
        Medicamento medicamento = new Medicamento();
        assertNotNull(medicamento);
    }

    @Test
    void testMedicamentoEqualsAndHashCode() {
        Medicamento med1 = new Medicamento();
        med1.setId(1L);
        med1.setNombre("Acetaminofén");

        Medicamento med2 = new Medicamento();
        med2.setId(1L);
        med2.setNombre("Acetaminofén");

        assertEquals(med1, med2);
        assertEquals(med1.hashCode(), med2.hashCode());
    }
}
