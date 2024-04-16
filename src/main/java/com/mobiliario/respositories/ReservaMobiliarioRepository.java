package com.mobiliario.respositories;

import com.mobiliario.models.ReservaMobiliarioEntity;
import com.mobiliario.models.ReservaMobiliarioEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaMobiliarioRepository extends JpaRepository<ReservaMobiliarioEntity, ReservaMobiliarioEntityId> {
}
