package com.example.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "RolesEntity")
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Enumerated(EnumType.STRING)
    private ERole rol;

    public RolesEntity(ERole rol) {
        this.rol=rol;
    }
}
