package com.medicamentos.solicitudes.dto;

import com.medicamentos.solicitudes.validator.ValidSolicitudNoPOS;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidSolicitudNoPOS
public class SolicitudRequest {
    
    @NotNull(message = "El ID del medicamento es obligatorio")
    private Long medicamentoId;
    
    private String numeroOrden;
    
    private String direccion;
    
    private String telefono;
    
    @Email(message = "El correo debe ser válido")
    private String correo;
}
