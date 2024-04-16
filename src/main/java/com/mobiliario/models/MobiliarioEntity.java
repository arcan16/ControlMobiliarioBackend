package com.mobiliario.models;

import com.mobiliario.dto.mobiliario.CreateMobiliarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "MobiliarioEntity")
@Table(name = "mobiliario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idMobiliario")
public class MobiliarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMobiliario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo")
    private TipoMobiliarioEntity tipo;

    private String descripcion;
    private Integer cantidad;
    @CreatedDate
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @CreatedDate
    private LocalDateTime ultimaActualizacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private UserEntity usuario;


    public MobiliarioEntity(TipoMobiliarioEntity tipoMobiliario, CreateMobiliarioDTO mobiliarioDTO, UserEntity user) {
        this.tipo = tipoMobiliario;
        this.descripcion = mobiliarioDTO.descripcion();
        this.cantidad = mobiliarioDTO.cantidad();
        this.usuario = user;
    }
}
