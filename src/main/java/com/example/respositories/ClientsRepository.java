package com.example.respositories;

import com.example.models.ClientsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientsRepository extends JpaRepository<ClientsEntity, Long> {

    boolean existsByNombre(String nombre);

    @Query("""
            SELECT c FROM ClientsEntity c WHERE c.nombre = :cliente
            """)
    Optional<ClientsEntity> getClientByName(String cliente);
}
