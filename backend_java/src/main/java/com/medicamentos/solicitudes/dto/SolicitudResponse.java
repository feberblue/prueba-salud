package com.medicamentos.solicitudes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudResponse {
    private Long id;
    private Long usuarioId;
    private String usuarioUsername;
    private MedicamentoResponse medicamento;
    private String numeroOrden;
    private String direccion;
    private String telefono;
    private String correo;
    private String estado;
    private LocalDateTime createdAt;
}
