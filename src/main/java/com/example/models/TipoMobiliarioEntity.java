package com.example.models;

import com.example.dto.mobiliario.tipo.CreateTipoMobiliarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "TipoMobiliarioEntity")
@Table(name = "tipo_mobiliario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoMobiliarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipo;

    private String nombre;

    @CreatedDate
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private UserEntity usuario;

    public TipoMobiliarioEntity(CreateTipoMobiliarioDTO createTipoMobiliarioDTO, UserEntity usuario) {
        this.nombre = createTipoMobiliarioDTO.nombre();
        this.usuario = usuario;
    }
}