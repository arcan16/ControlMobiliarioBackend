package com.example.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "ReservaMobiliarioEntity")
@Table(name = "reserva_mobiliario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ReservaMobiliarioEntity {

    @EmbeddedId
    private ReservaMobiliarioEntityId id;

    @ManyToOne
    @MapsId("idReservacion")
    @JoinColumn(name = "id_reservacion")
    private ReservacionEntity reservacion;

    @ManyToOne
    @MapsId("idPresentacion")
    @JoinColumn(name = "id_presentacion")
    @JsonManagedReference
    private PresentationEntity presentation;

    private Integer cantidad;

}
