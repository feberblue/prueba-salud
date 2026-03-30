package com.medicamentos.solicitudes.controller;

import com.medicamentos.solicitudes.dto.MedicamentoResponse;
import com.medicamentos.solicitudes.dto.SolicitudRequest;
import com.medicamentos.solicitudes.dto.SolicitudResponse;
import com.medicamentos.solicitudes.service.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SolicitudController {
    
    private final SolicitudService solicitudService;
    
    @GetMapping("/medicamentos")
    public ResponseEntity<List<MedicamentoResponse>> getAllMedicamentos() {
        List<MedicamentoResponse> medicamentos = solicitudService.getAllMedicamentos();
        return ResponseEntity.ok(medicamentos);
    }
    
    @PostMapping("/solicitudes")
    public ResponseEntity<Map<String, Object>> createSolicitud(@Valid @RequestBody SolicitudRequest request) {
        SolicitudResponse solicitud = solicitudService.createSolicitud(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Solicitud creada exitosamente");
        response.put("solicitud", solicitud);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/solicitudes")
    public ResponseEntity<Map<String, Object>> getMySolicitudes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<SolicitudResponse> solicitudes = solicitudService.getMySolicitudes(page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("solicitudes", solicitudes.getContent());
        response.put("currentPage", solicitudes.getNumber());
        response.put("totalItems", solicitudes.getTotalElements());
        response.put("totalPages", solicitudes.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
}
