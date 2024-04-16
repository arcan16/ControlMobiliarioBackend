package com.mobiliario.models;

import com.mobiliario.dto.reservacion.CreateReservacionDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity(name = "ReservacionEntity")
@Table(name = "reservacion")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "reservaMobiliarioList")
public class ReservacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservacion;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClientsEntity cliente;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UserEntity usuario;

    @CreatedDate
    private LocalDateTime fechaReserva = LocalDateTime.now();

    private String direccionEntrega;

    private LocalDateTime fechaEntrega;

    private LocalDateTime fechaRecepcion;

    private Integer status=0    ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservacion", fetch = FetchType.LAZY)
    private List<ReservaMobiliarioEntity> reservaMobiliarioList = new ArrayList<>();

    public ReservacionEntity(ClientsEntity cliente, UserEntity actualUser, CreateReservacionDTO reservacionDTO) {
        this.cliente = cliente;
        this.usuario = actualUser;
        this.fechaEntrega = reservacionDTO.fechaEntrega();
        this.fechaRecepcion = reservacionDTO.fechaRecepcion();
        this.direccionEntrega = reservacionDTO.direccionEntrega();
    }


}
