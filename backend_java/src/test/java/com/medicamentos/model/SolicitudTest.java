package com.medicamentos.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    @Test
    void testSolicitudCreation() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        Medicamento medicamento = new Medicamento();
        medicamento.setId(1L);
        medicamento.setNombre("Acetaminofén");

        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setUsuario(usuario);
        solicitud.setMedicamento(medicamento);
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        solicitud.setCreatedAt(LocalDateTime.now());

        assertEquals(1L, solicitud.getId());
        assertEquals(usuario, solicitud.getUsuario());
        assertEquals(medicamento, solicitud.getMedicamento());
        assertEquals(Solicitud.EstadoSolicitud.PENDIENTE, solicitud.getEstado());
        assertNotNull(solicitud.getCreatedAt());
    }

    @Test
    void testSolicitudNoPOS() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Medicamento medicamento = new Medicamento();
        medicamento.setId(2L);
        medicamento.setEsNoPos(true);

        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setUsuario(usuario);
        solicitud.setMedicamento(medicamento);
        solicitud.setNumeroOrden("ORD-001");
        solicitud.setDireccion("Calle 123");
        solicitud.setTelefono("3001234567");
        solicitud.setCorreo("test@example.com");
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);

        assertEquals("ORD-001", solicitud.getNumeroOrden());
        assertEquals("Calle 123", solicitud.getDireccion());
        assertEquals("3001234567", solicitud.getTelefono());
        assertEquals("test@example.com", solicitud.getCorreo());
    }

    @Test
    void testEstadoSolicitudEnum() {
        assertEquals("PENDIENTE", Solicitud.EstadoSolicitud.PENDIENTE.name());
        assertEquals("APROBADA", Solicitud.EstadoSolicitud.APROBADA.name());
        assertEquals("RECHAZADA", Solicitud.EstadoSolicitud.RECHAZADA.name());
    }

    @Test
    void testSolicitudAllArgsConstructor() {
        Usuario usuario = new Usuario();
        Medicamento medicamento = new Medicamento();
        LocalDateTime now = LocalDateTime.now();

        Solicitud solicitud = new Solicitud(
            1L,
            usuario,
            medicamento,
            "ORD-001",
            "Calle 123",
            "3001234567",
            "test@example.com",
            Solicitud.EstadoSolicitud.PENDIENTE,
            now
        );

        assertEquals(1L, solicitud.getId());
        assertEquals(usuario, solicitud.getUsuario());
        assertEquals(medicamento, solicitud.getMedicamento());
        assertEquals("ORD-001", solicitud.getNumeroOrden());
        assertEquals(now, solicitud.getCreatedAt());
    }

    @Test
    void testSolicitudNoArgsConstructor() {
        Solicitud solicitud = new Solicitud();
        assertNotNull(solicitud);
    }

    @Test
    void testSolicitudEstadoTransitions() {
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        assertEquals(Solicitud.EstadoSolicitud.PENDIENTE, solicitud.getEstado());

        solicitud.setEstado(Solicitud.EstadoSolicitud.APROBADA);
        assertEquals(Solicitud.EstadoSolicitud.APROBADA, solicitud.getEstado());

        solicitud.setEstado(Solicitud.EstadoSolicitud.RECHAZADA);
        assertEquals(Solicitud.EstadoSolicitud.RECHAZADA, solicitud.getEstado());
    }
}
