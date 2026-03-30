package com.medicamentos.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void testNotFoundExceptionMessage() {
        String message = "Recurso no encontrado";
        NotFoundException exception = new NotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void testNotFoundExceptionIsRuntimeException() {
        NotFoundException exception = new NotFoundException("Test");
        assertTrue(exception instanceof RuntimeException);
    }
}
