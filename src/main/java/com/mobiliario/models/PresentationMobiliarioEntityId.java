package com.mobiliario.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class PresentationMobiliarioEntityId implements Serializable {
    @Column(name = "id_presentacion")
    private Long idPresentacion;

    @Column(name = "id_mobiliario")
    private Long idMobiliario;
}
