package com.medicamentos.solicitudes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicamentos.solicitudes.dto.MedicamentoResponse;
import com.medicamentos.solicitudes.dto.SolicitudRequest;
import com.medicamentos.solicitudes.dto.SolicitudResponse;
import com.medicamentos.solicitudes.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
@AutoConfigureMockMvc(addFilters = false)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolicitudService solicitudService;

    private MedicamentoResponse medicamentoResponse;
    private SolicitudResponse solicitudResponse;
    private SolicitudRequest solicitudRequest;

    @BeforeEach
    void setUp() {
        medicamentoResponse = new MedicamentoResponse(
            1L,
            "Acetaminofén 500mg",
            "Analgésico",
            false
        );

        solicitudResponse = new SolicitudResponse(
            1L,
            1L,
            "testuser",
            medicamentoResponse,
            null,
            null,
            null,
            null,
            "PENDIENTE",
            LocalDateTime.now()
        );

        solicitudRequest = new SolicitudRequest();
        solicitudRequest.setMedicamentoId(1L);
    }

    @Test
    @WithMockUser
    void getAllMedicamentos_Success() throws Exception {
        List<MedicamentoResponse> medicamentos = Arrays.asList(medicamentoResponse);
        when(solicitudService.getAllMedicamentos()).thenReturn(medicamentos);

        mockMvc.perform(get("/api/medicamentos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Acetaminofén 500mg"))
            .andExpect(jsonPath("$[0].esNoPos").value(false));
    }

    @Test
    @WithMockUser
    void createSolicitud_Success() throws Exception {
        when(solicitudService.createSolicitud(any(SolicitudRequest.class)))
            .thenReturn(solicitudResponse);

        mockMvc.perform(post("/api/solicitudes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitudRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Solicitud creada exitosamente"))
            .andExpect(jsonPath("$.solicitud.usuarioUsername").value("testuser"))
            .andExpect(jsonPath("$.solicitud.estado").value("PENDIENTE"));
    }

    @Test
    @WithMockUser
    void createSolicitud_ValidationError() throws Exception {
        SolicitudRequest invalidRequest = new SolicitudRequest();

        mockMvc.perform(post("/api/solicitudes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getMySolicitudes_Success() throws Exception {
        List<SolicitudResponse> solicitudes = Arrays.asList(solicitudResponse);
        Page<SolicitudResponse> page = new PageImpl<>(solicitudes);

        when(solicitudService.getMySolicitudes(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/solicitudes")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.solicitudes[0].usuarioUsername").value("testuser"))
            .andExpect(jsonPath("$.currentPage").value(0))
            .andExpect(jsonPath("$.totalItems").value(1))
            .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @WithMockUser
    void getMySolicitudes_DefaultPagination() throws Exception {
        List<SolicitudResponse> solicitudes = Arrays.asList(solicitudResponse);
        Page<SolicitudResponse> page = new PageImpl<>(solicitudes);

        when(solicitudService.getMySolicitudes(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/solicitudes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.solicitudes").isArray())
            .andExpect(jsonPath("$.currentPage").exists())
            .andExpect(jsonPath("$.totalItems").exists());
    }
}
