package com.example.respositories;

import com.example.models.PresentationEntity;
import com.example.models.PresentationMobiliarioEntity;
import com.example.models.PresentationMobiliarioEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PresentationMobiliarioRepository extends JpaRepository<PresentationMobiliarioEntity, Long> {
    void deleteByPresentation(PresentationEntity presentationToUpdate);

    void deleteById(PresentationMobiliarioEntityId id);


    @Modifying
    @Query("""
            delete from PresentationMobiliarioEntity pm where pm.presentation.idPresentacion = :id
            """)
    @Transactional
    int deleteAllByPresentation(Long id);
}
