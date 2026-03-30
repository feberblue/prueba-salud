package com.medicamentos.auth.repository;

import com.medicamentos.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void findByUsername_Success() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");
        entityManager.persist(usuario);
        entityManager.flush();

        Optional<Usuario> found = usuarioRepository.findByUsername("testuser");

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void findByUsername_NotFound() {
        Optional<Usuario> found = usuarioRepository.findByUsername("nonexistent");
        assertFalse(found.isPresent());
    }

    @Test
    void findByEmail_Success() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");
        entityManager.persist(usuario);
        entityManager.flush();

        Optional<Usuario> found = usuarioRepository.findByEmail("test@example.com");

        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void existsByUsername_True() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");
        entityManager.persist(usuario);
        entityManager.flush();

        boolean exists = usuarioRepository.existsByUsername("testuser");
        assertTrue(exists);
    }

    @Test
    void existsByUsername_False() {
        boolean exists = usuarioRepository.existsByUsername("nonexistent");
        assertFalse(exists);
    }

    @Test
    void existsByEmail_True() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");
        entityManager.persist(usuario);
        entityManager.flush();

        boolean exists = usuarioRepository.existsByEmail("test@example.com");
        assertTrue(exists);
    }

    @Test
    void existsByEmail_False() {
        boolean exists = usuarioRepository.existsByEmail("nonexistent@example.com");
        assertFalse(exists);
    }
}
