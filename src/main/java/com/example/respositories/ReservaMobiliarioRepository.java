package com.example.respositories;

import com.example.models.ReservaMobiliarioEntity;
import com.example.models.ReservaMobiliarioEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaMobiliarioRepository extends JpaRepository<ReservaMobiliarioEntity, ReservaMobiliarioEntityId> {
}
