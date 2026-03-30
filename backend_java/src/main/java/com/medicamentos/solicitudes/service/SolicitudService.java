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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    
    private final SolicitudRepository solicitudRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final UsuarioRepository usuarioRepository;
    
    public List<MedicamentoResponse> getAllMedicamentos() {
        return medicamentoRepository.findAllByOrderByNombreAsc().stream()
            .map(this::mapToMedicamentoResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public SolicitudResponse createSolicitud(SolicitudRequest request) {
        Usuario usuario = getCurrentUser();
        
        Medicamento medicamento = medicamentoRepository.findById(request.getMedicamentoId())
            .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));
        
        if (medicamento.getEsNoPos()) {
            validateNoPosFields(request);
        }
        
        Solicitud solicitud = new Solicitud();
        solicitud.setUsuario(usuario);
        solicitud.setMedicamento(medicamento);
        solicitud.setNumeroOrden(request.getNumeroOrden());
        solicitud.setDireccion(request.getDireccion());
        solicitud.setTelefono(request.getTelefono());
        solicitud.setCorreo(request.getCorreo());
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        
        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        
        return mapToSolicitudResponse(savedSolicitud);
    }
    
    public Page<SolicitudResponse> getMySolicitudes(int page, int size) {
        Usuario usuario = getCurrentUser();
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Solicitud> solicitudes = solicitudRepository.findByUsuarioIdOrderByCreatedAtDesc(
            usuario.getId(), pageable
        );
        
        return solicitudes.map(this::mapToSolicitudResponse);
    }
    
    private void validateNoPosFields(SolicitudRequest request) {
        if (request.getNumeroOrden() == null || request.getNumeroOrden().trim().isEmpty()) {
            throw new BadRequestException("El número de orden es obligatorio para medicamentos NO POS");
        }
        if (request.getDireccion() == null || request.getDireccion().trim().isEmpty()) {
            throw new BadRequestException("La dirección es obligatoria para medicamentos NO POS");
        }
        if (request.getTelefono() == null || request.getTelefono().trim().isEmpty()) {
            throw new BadRequestException("El teléfono es obligatorio para medicamentos NO POS");
        }
        if (request.getCorreo() == null || request.getCorreo().trim().isEmpty()) {
            throw new BadRequestException("El correo electrónico es obligatorio para medicamentos NO POS");
        }
    }
    
    private Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }
    
    private MedicamentoResponse mapToMedicamentoResponse(Medicamento medicamento) {
        return new MedicamentoResponse(
            medicamento.getId(),
            medicamento.getNombre(),
            medicamento.getDescripcion(),
            medicamento.getEsNoPos()
        );
    }
    
    private SolicitudResponse mapToSolicitudResponse(Solicitud solicitud) {
        return new SolicitudResponse(
            solicitud.getId(),
            solicitud.getUsuario().getId(),
            solicitud.getUsuario().getUsername(),
            mapToMedicamentoResponse(solicitud.getMedicamento()),
            solicitud.getNumeroOrden(),
            solicitud.getDireccion(),
            solicitud.getTelefono(),
            solicitud.getCorreo(),
            solicitud.getEstado().name(),
            solicitud.getCreatedAt()
        );
    }
}
