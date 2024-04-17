package com.example.respositories;

import com.example.models.MobiliarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobiliarioRepository extends JpaRepository<MobiliarioEntity, Long> {
    boolean existsByDescripcion(String descripcion);
}
