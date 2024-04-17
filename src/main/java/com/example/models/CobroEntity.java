package com.example.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "CobroEntity")
@Table(name = "cobro")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CobroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCobro;

    @OneToOne
    @JoinColumn(name = "id_reservacion")
    private ReservacionEntity reservacion;

    private Float total;

    @CreatedDate
    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_usuario" )
    private UserEntity usuario;

    private Boolean valido;

}
