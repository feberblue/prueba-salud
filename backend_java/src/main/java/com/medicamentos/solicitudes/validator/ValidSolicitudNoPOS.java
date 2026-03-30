package com.medicamentos.solicitudes.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SolicitudNoPOSValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSolicitudNoPOS {
    String message() default "Los campos adicionales son obligatorios para medicamentos NO POS";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
