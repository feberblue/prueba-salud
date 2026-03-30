package com.medicamentos.solicitudes.repository;

import com.medicamentos.model.Medicamento;
import com.medicamentos.model.Solicitud;
import com.medicamentos.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SolicitudRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SolicitudRepository solicitudRepository;

    private Usuario usuario;
    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");
        entityManager.persist(usuario);

        medicamento = new Medicamento();
        medicamento.setNombre("Acetaminofén");
        medicamento.setDescripcion("Analgésico");
        medicamento.setEsNoPos(false);
        entityManager.persist(medicamento);

        entityManager.flush();
    }

    @Test
    void findByUsuarioIdOrderByCreatedAtDesc_Success() {
        Solicitud solicitud1 = new Solicitud();
        solicitud1.setUsuario(usuario);
        solicitud1.setMedicamento(medicamento);
        solicitud1.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        entityManager.persist(solicitud1);

        Solicitud solicitud2 = new Solicitud();
        solicitud2.setUsuario(usuario);
        solicitud2.setMedicamento(medicamento);
        solicitud2.setEstado(Solicitud.EstadoSolicitud.APROBADA);
        entityManager.persist(solicitud2);

        entityManager.flush();

        Page<Solicitud> solicitudes = solicitudRepository.findByUsuarioIdOrderByCreatedAtDesc(
            usuario.getId(),
            PageRequest.of(0, 10)
        );

        assertEquals(2, solicitudes.getTotalElements());
        assertEquals(usuario.getId(), solicitudes.getContent().get(0).getUsuario().getId());
    }

    @Test
    void findByUsuarioIdOrderByCreatedAtDesc_EmptyPage() {
        Page<Solicitud> solicitudes = solicitudRepository.findByUsuarioIdOrderByCreatedAtDesc(
            999L,
            PageRequest.of(0, 10)
        );

        assertEquals(0, solicitudes.getTotalElements());
        assertTrue(solicitudes.getContent().isEmpty());
    }

    @Test
    void findByUsuarioIdOrderByCreatedAtDesc_Pagination() {
        for (int i = 0; i < 15; i++) {
            Solicitud solicitud = new Solicitud();
            solicitud.setUsuario(usuario);
            solicitud.setMedicamento(medicamento);
            solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
            entityManager.persist(solicitud);
        }
        entityManager.flush();

        Page<Solicitud> page1 = solicitudRepository.findByUsuarioIdOrderByCreatedAtDesc(
            usuario.getId(),
            PageRequest.of(0, 10)
        );

        Page<Solicitud> page2 = solicitudRepository.findByUsuarioIdOrderByCreatedAtDesc(
            usuario.getId(),
            PageRequest.of(1, 10)
        );

        assertEquals(15, page1.getTotalElements());
        assertEquals(10, page1.getContent().size());
        assertEquals(5, page2.getContent().size());
    }

    @Test
    void saveSolicitud_Success() {
        Solicitud solicitud = new Solicitud();
        solicitud.setUsuario(usuario);
        solicitud.setMedicamento(medicamento);
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);

        Solicitud saved = solicitudRepository.save(solicitud);

        assertNotNull(saved.getId());
        assertEquals(Solicitud.EstadoSolicitud.PENDIENTE, saved.getEstado());
    }
}
