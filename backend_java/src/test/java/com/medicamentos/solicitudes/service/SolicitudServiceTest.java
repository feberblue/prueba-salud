package com.medicamentos.solicitudes.service;

import com.medicamentos.auth.repository.UsuarioRepository;
import com.medicamentos.exception.BadRequestException;
import com.medicamentos.exception.NotFoundException;
import com.medicamentos.model.Medicamento;
import com.medicamentos.model.Solicitud;
import com.medicamentos.model.Usuario;
import com.medicamentos.solicitudes.dto.MedicamentoResponse;
import com.medicamentos.solicitudes.dto.SolicitudRequest;
import com.medicamentos.solicitudes.dto.SolicitudResponse;
import com.medicamentos.solicitudes.repository.MedicamentoRepository;
import com.medicamentos.solicitudes.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SolicitudService solicitudService;

    private Usuario usuario;
    private Medicamento medicamentoPOS;
    private Medicamento medicamentoNoPOS;
    private Solicitud solicitud;
    private SolicitudRequest solicitudRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");
        usuario.setCreatedAt(LocalDateTime.now());

        medicamentoPOS = new Medicamento();
        medicamentoPOS.setId(1L);
        medicamentoPOS.setNombre("Acetaminofén 500mg");
        medicamentoPOS.setDescripcion("Analgésico");
        medicamentoPOS.setEsNoPos(false);
        medicamentoPOS.setCreatedAt(LocalDateTime.now());

        medicamentoNoPOS = new Medicamento();
        medicamentoNoPOS.setId(2L);
        medicamentoNoPOS.setNombre("Adalimumab 40mg");
        medicamentoNoPOS.setDescripcion("Medicamento biológico");
        medicamentoNoPOS.setEsNoPos(true);
        medicamentoNoPOS.setCreatedAt(LocalDateTime.now());

        solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setUsuario(usuario);
        solicitud.setMedicamento(medicamentoPOS);
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        solicitud.setCreatedAt(LocalDateTime.now());

        solicitudRequest = new SolicitudRequest();
        solicitudRequest.setMedicamentoId(1L);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void getAllMedicamentos_Success() {
        List<Medicamento> medicamentos = Arrays.asList(medicamentoPOS, medicamentoNoPOS);
        when(medicamentoRepository.findAllByOrderByNombreAsc()).thenReturn(medicamentos);

        List<MedicamentoResponse> responses = solicitudService.getAllMedicamentos();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Acetaminofén 500mg", responses.get(0).getNombre());
        assertEquals("Adalimumab 40mg", responses.get(1).getNombre());
        verify(medicamentoRepository).findAllByOrderByNombreAsc();
    }

    @Test
    void createSolicitud_POSMedicamento_Success() {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(medicamentoPOS));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitud);

        SolicitudResponse response = solicitudService.createSolicitud(solicitudRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsuarioUsername());
        assertEquals("Acetaminofén 500mg", response.getMedicamento().getNombre());
        verify(solicitudRepository).save(any(Solicitud.class));
    }

    @Test
    void createSolicitud_NoPOSMedicamento_Success() {
        solicitudRequest.setMedicamentoId(2L);
        solicitudRequest.setNumeroOrden("ORD-001");
        solicitudRequest.setDireccion("Calle 123");
        solicitudRequest.setTelefono("3001234567");
        solicitudRequest.setCorreo("test@example.com");

        Solicitud solicitudNoPOS = new Solicitud();
        solicitudNoPOS.setId(2L);
        solicitudNoPOS.setUsuario(usuario);
        solicitudNoPOS.setMedicamento(medicamentoNoPOS);
        solicitudNoPOS.setNumeroOrden("ORD-001");
        solicitudNoPOS.setDireccion("Calle 123");
        solicitudNoPOS.setTelefono("3001234567");
        solicitudNoPOS.setCorreo("test@example.com");
        solicitudNoPOS.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        solicitudNoPOS.setCreatedAt(LocalDateTime.now());

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById(2L)).thenReturn(Optional.of(medicamentoNoPOS));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudNoPOS);

        SolicitudResponse response = solicitudService.createSolicitud(solicitudRequest);

        assertNotNull(response);
        assertEquals("ORD-001", response.getNumeroOrden());
        assertEquals("Calle 123", response.getDireccion());
        verify(solicitudRepository).save(any(Solicitud.class));
    }

    @Test
    void createSolicitud_MedicamentoNotFound() {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> solicitudService.createSolicitud(solicitudRequest)
        );

        assertEquals("Medicamento no encontrado", exception.getMessage());
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void createSolicitud_NoPOSMedicamento_MissingNumeroOrden() {
        solicitudRequest.setMedicamentoId(2L);
        solicitudRequest.setDireccion("Calle 123");
        solicitudRequest.setTelefono("3001234567");
        solicitudRequest.setCorreo("test@example.com");

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById(2L)).thenReturn(Optional.of(medicamentoNoPOS));

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> solicitudService.createSolicitud(solicitudRequest)
        );

        assertEquals("El número de orden es obligatorio para medicamentos NO POS", exception.getMessage());
    }

    @Test
    void createSolicitud_NoPOSMedicamento_MissingDireccion() {
        solicitudRequest.setMedicamentoId(2L);
        solicitudRequest.setNumeroOrden("ORD-001");
        solicitudRequest.setTelefono("3001234567");
        solicitudRequest.setCorreo("test@example.com");

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById(2L)).thenReturn(Optional.of(medicamentoNoPOS));

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> solicitudService.createSolicitud(solicitudRequest)
        );

        assertEquals("La dirección es obligatoria para medicamentos NO POS", exception.getMessage());
    }

    @Test
    void createSolicitud_NoPOSMedicamento_MissingTelefono() {
        solicitudRequest.setMedicamentoId(2L);
        solicitudRequest.setNumeroOrden("ORD-001");
        solicitudRequest.setDireccion("Calle 123");
        solicitudRequest.setCorreo("test@example.com");

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById(2L)).thenReturn(Optional.of(medicamentoNoPOS));

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> solicitudService.createSolicitud(solicitudRequest)
        );

        assertEquals("El teléfono es obligatorio para medicamentos NO POS", exception.getMessage());
    }

    @Test
    void createSolicitud_NoPOSMedicamento_MissingCorreo() {
        solicitudRequest.setMedicamentoId(2L);
        solicitudRequest.setNumeroOrden("ORD-001");
        solicitudRequest.setDireccion("Calle 123");
        solicitudRequest.setTelefono("3001234567");

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(medicamentoRepository.findById(2L)).thenReturn(Optional.of(medicamentoNoPOS));

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> solicitudService.createSolicitud(solicitudRequest)
        );

        assertEquals("El correo electrónico es obligatorio para medicamentos NO POS", exception.getMessage());
    }

    @Test
    void getMySolicitudes_Success() {
        List<Solicitud> solicitudes = Arrays.asList(solicitud);
        Page<Solicitud> page = new PageImpl<>(solicitudes);

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(solicitudRepository.findByUsuarioIdOrderByCreatedAtDesc(any(Long.class), any(Pageable.class)))
            .thenReturn(page);

        Page<SolicitudResponse> responses = solicitudService.getMySolicitudes(0, 10);

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        assertEquals("testuser", responses.getContent().get(0).getUsuarioUsername());
        verify(solicitudRepository).findByUsuarioIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class));
    }

    @Test
    void getMySolicitudes_UserNotFound() {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> solicitudService.getMySolicitudes(0, 10)
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
    }
}
