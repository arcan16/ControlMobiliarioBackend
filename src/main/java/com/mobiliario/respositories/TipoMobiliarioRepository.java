package com.mobiliario.respositories;

import com.mobiliario.models.TipoMobiliarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMobiliarioRepository extends JpaRepository<TipoMobiliarioEntity, Long> {
    boolean existsByNombre(String nombre);
}
