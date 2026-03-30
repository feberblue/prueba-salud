package com.medicamentos.auth.service;

import com.medicamentos.auth.dto.LoginRequest;
import com.medicamentos.auth.dto.RegisterRequest;
import com.medicamentos.auth.dto.UserResponse;
import com.medicamentos.auth.repository.UsuarioRepository;
import com.medicamentos.exception.BadRequestException;
import com.medicamentos.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession httpSession;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("encodedPassword");
        usuario.setCreatedAt(LocalDateTime.now());

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void register_Success() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UserResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void register_UsernameAlreadyExists() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(true);

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> authService.register(registerRequest)
        );

        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void register_EmailAlreadyExists() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> authService.register(registerRequest)
        );

        assertEquals("El email ya está registrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void login_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        UserResponse response = authService.login(loginRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(httpSession).setAttribute(anyString(), any(SecurityContext.class));
    }

    @Test
    void login_UserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        SecurityContextHolder.setContext(securityContext);

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> authService.login(loginRequest, httpServletRequest)
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void logout_Success() {
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);

        authService.logout(httpServletRequest);

        verify(httpSession).invalidate();
    }

    @Test
    void logout_NoSession() {
        when(httpServletRequest.getSession(false)).thenReturn(null);

        assertDoesNotThrow(() -> authService.logout(httpServletRequest));
    }

    @Test
    void getCurrentUser_Success() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getPrincipal()).thenReturn("testuser");
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));

        UserResponse response = authService.getCurrentUser();

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void getCurrentUser_NotAuthenticated() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        UserResponse response = authService.getCurrentUser();

        assertNull(response);
    }

    @Test
    void getCurrentUser_AnonymousUser() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        UserResponse response = authService.getCurrentUser();

        assertNull(response);
    }

    @Test
    void getCurrentUser_UserNotFoundInDatabase() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getPrincipal()).thenReturn("testuser");
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> authService.getCurrentUser()
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
    }
}
