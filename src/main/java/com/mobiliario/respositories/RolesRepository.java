package com.mobiliario.respositories;

import com.mobiliario.models.ERole;
import com.mobiliario.models.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    boolean existsByRol(ERole rol);

    RolesEntity findByRol(ERole rol);

}
