package com.example.respositories;

import com.example.models.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {

    @Query("""
            select e from EmployeeEntity e where usuario.idUsuario = :idUsuario
            """)
    Optional<EmployeeEntity> findByUsuario(Long idUsuario);


    @Query("""
            SELECT COUNT(e) FROM EmployeeEntity e WHERE e.nombre= :nombre AND e.apellido = :apellido
            """)
    int existsByNameLastname(String nombre, String apellido);


    @Query("""
            SELECT e FROM EmployeeEntity e WHERE e.nombre= :nombre AND e.apellido = :apellido
            """)
    Optional<EmployeeEntity> findByNombreApellido(String nombre, String apellido);

    @Query("""
            SELECT e FROM EmployeeEntity e WHERE e.usuario.idUsuario = :id
            """)
    EmployeeEntity findByIdUsuario(Long id);
}
