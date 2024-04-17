package com.example.respositories;

import com.example.models.CobroEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CobroRepository extends JpaRepository<CobroEntity,Long> {
    @Query("""
            select c from CobroEntity c where c.valido=true
            """)
    Page<CobroEntity> findByValid(Pageable pageable);

    @Query("""
            select c from CobroEntity c where c.valido=false
            """)
    Page<CobroEntity> findByCanceled(Pageable pageable);

    @Query("""
            select c from CobroEntity c where date(c.fecha) = date(:fecha)
            """)
    List<CobroEntity> findAllByFecha(LocalDateTime fecha);
}
