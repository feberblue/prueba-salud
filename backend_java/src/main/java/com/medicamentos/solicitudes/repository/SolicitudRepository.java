package com.medicamentos.solicitudes.repository;

import com.medicamentos.model.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    Page<Solicitud> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId, Pageable pageable);
}
