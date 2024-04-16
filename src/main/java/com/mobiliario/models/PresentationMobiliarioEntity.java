package com.mobiliario.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "PresentationMobiliarioEntity")
@Table(name = "presentacion_mobiliario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PresentationMobiliarioEntity {

    @EmbeddedId
    private PresentationMobiliarioEntityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPresentacion")
    @JoinColumn(name = "id_presentacion")
    @JsonBackReference
    private PresentationEntity presentation;

    @ManyToOne
    @MapsId("idMobiliario")
    @JoinColumn(name = "id_mobiliario", insertable = false)
    private MobiliarioEntity mobiliario;

    @Column(name = "cantidad")
    private int cantidad;


    public PresentationMobiliarioEntity(PresentationMobiliarioEntityId id,
                                        MobiliarioEntity mobi,
                                        PresentationEntity presentacionGuardada,
                                        Integer cantidad) {
        this.id=id;
        this.mobiliario=mobi;
        this.presentation=presentacionGuardada;
        this.cantidad=cantidad;
    }



    public void setPresentation(PresentationEntity presentationEntity) {
        this.presentation = presentationEntity;
        presentationEntity.getPresentationMobiliarioEntityList().add(this);
    }
}
