package com.example.respositories;

import com.example.models.PresentationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PresentationRepository extends JpaRepository<PresentationEntity, Long> {
    boolean existsByDescripcion(String description);

    @Query("SELECT p FROM PresentationEntity p LEFT JOIN FETCH p.usuario WHERE p.idPresentacion = :id")
    PresentationEntity findByIdWithUsuario(@Param("id") Long id);

}
