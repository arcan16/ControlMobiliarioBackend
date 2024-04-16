package com.mobiliario.respositories;

import com.mobiliario.models.PresentationEntity;
import com.mobiliario.models.PresentationMobiliarioEntity;
import com.mobiliario.models.PresentationMobiliarioEntityId;
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
