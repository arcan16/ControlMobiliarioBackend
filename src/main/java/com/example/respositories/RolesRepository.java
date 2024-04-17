package com.example.respositories;

import com.example.models.ERole;
import com.example.models.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    boolean existsByRol(ERole rol);

    RolesEntity findByRol(ERole rol);

}
