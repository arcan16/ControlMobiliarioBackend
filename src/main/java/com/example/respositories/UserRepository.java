package com.example.respositories;

import com.example.models.RolesEntity;
import com.example.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByRol(RolesEntity rol);

    boolean existsByUsername(String admin);

    void deleteAllByUsername(String admin);

    UserEntity getReferenceByUsername(String userActual);

    @Query("""
            SELECT u FROM UserEntity u WHERE u.username = :recoverData OR u.email = :recoverData
            """)
    UserEntity findByUsernameOrEmail(String recoverData);
}
