package com.medicamentos.solicitudes.repository;

import com.medicamentos.model.Medicamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MedicamentoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Test
    void findAllByOrderByNombreAsc_Success() {
        Medicamento med1 = new Medicamento();
        med1.setNombre("Ibuprofeno");
        med1.setDescripcion("Antiinflamatorio");
        med1.setEsNoPos(false);
        entityManager.persist(med1);

        Medicamento med2 = new Medicamento();
        med2.setNombre("Acetaminofén");
        med2.setDescripcion("Analgésico");
        med2.setEsNoPos(false);
        entityManager.persist(med2);

        Medicamento med3 = new Medicamento();
        med3.setNombre("Adalimumab");
        med3.setDescripcion("Biológico");
        med3.setEsNoPos(true);
        entityManager.persist(med3);

        entityManager.flush();

        List<Medicamento> medicamentos = medicamentoRepository.findAllByOrderByNombreAsc();

        assertEquals(3, medicamentos.size());
        assertEquals("Acetaminofén", medicamentos.get(0).getNombre());
        assertEquals("Adalimumab", medicamentos.get(1).getNombre());
        assertEquals("Ibuprofeno", medicamentos.get(2).getNombre());
    }

    @Test
    void findAllByOrderByNombreAsc_EmptyList() {
        List<Medicamento> medicamentos = medicamentoRepository.findAllByOrderByNombreAsc();
        assertTrue(medicamentos.isEmpty());
    }

    @Test
    void saveMedicamento_Success() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre("Test Medicamento");
        medicamento.setDescripcion("Test Description");
        medicamento.setEsNoPos(false);

        Medicamento saved = medicamentoRepository.save(medicamento);

        assertNotNull(saved.getId());
        assertEquals("Test Medicamento", saved.getNombre());
    }
}
