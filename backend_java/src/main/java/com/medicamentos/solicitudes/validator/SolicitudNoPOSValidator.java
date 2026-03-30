package com.medicamentos.solicitudes.validator;

import com.medicamentos.model.Medicamento;
import com.medicamentos.solicitudes.repository.MedicamentoRepository;
import com.medicamentos.solicitudes.dto.SolicitudRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudNoPOSValidator implements ConstraintValidator<ValidSolicitudNoPOS, SolicitudRequest> {

    private final MedicamentoRepository medicamentoRepository;

    @Override
    public boolean isValid(SolicitudRequest request, ConstraintValidatorContext context) {
        if (request.getMedicamentoId() == null) {
            return true;
        }

        Medicamento medicamento = medicamentoRepository.findById(request.getMedicamentoId())
                .orElse(null);

        if (medicamento == null || !medicamento.getEsNoPos()) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        boolean isValid = true;

        if (request.getNumeroOrden() == null || request.getNumeroOrden().trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate("El número de orden es obligatorio para medicamentos NO POS")
                    .addPropertyNode("numeroOrden")
                    .addConstraintViolation();
            isValid = false;
        }

        if (request.getDireccion() == null || request.getDireccion().trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate("La dirección es obligatoria para medicamentos NO POS")
                    .addPropertyNode("direccion")
                    .addConstraintViolation();
            isValid = false;
        }

        if (request.getTelefono() == null || request.getTelefono().trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate("El teléfono es obligatorio para medicamentos NO POS")
                    .addPropertyNode("telefono")
                    .addConstraintViolation();
            isValid = false;
        }

        if (request.getCorreo() == null || request.getCorreo().trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate("El correo es obligatorio para medicamentos NO POS")
                    .addPropertyNode("correo")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
