package com.medicamentos.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void testBadRequestExceptionMessage() {
        String message = "Error de solicitud";
        BadRequestException exception = new BadRequestException(message);

        assertEquals(message, exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void testBadRequestExceptionIsRuntimeException() {
        BadRequestException exception = new BadRequestException("Test");
        assertTrue(exception instanceof RuntimeException);
    }
}
