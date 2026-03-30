package com.medicamentos.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testUsuarioCreation() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setCreatedAt(LocalDateTime.now());

        assertEquals(1L, usuario.getId());
        assertEquals("testuser", usuario.getUsername());
        assertEquals("test@example.com", usuario.getEmail());
        assertEquals("password123", usuario.getPassword());
        assertNotNull(usuario.getCreatedAt());
    }

    @Test
    void testUsuarioAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Usuario usuario = new Usuario(1L, "testuser", "test@example.com", "password123", now);

        assertEquals(1L, usuario.getId());
        assertEquals("testuser", usuario.getUsername());
        assertEquals("test@example.com", usuario.getEmail());
        assertEquals("password123", usuario.getPassword());
        assertEquals(now, usuario.getCreatedAt());
    }

    @Test
    void testUsuarioNoArgsConstructor() {
        Usuario usuario = new Usuario();
        assertNotNull(usuario);
    }

    @Test
    void testUsuarioEqualsAndHashCode() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("testuser");

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setUsername("testuser");

        assertEquals(usuario1, usuario2);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void testUsuarioToString() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        String toString = usuario.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("testuser"));
    }
}
