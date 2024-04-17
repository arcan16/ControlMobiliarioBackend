package com.example.models;

import com.example.dto.presentation.CreatePresentationDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name="PresentationEntity")
@Table(name = "presentacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idPresentacion")
@ToString(exclude = "presentationMobiliarioEntityList")
public class PresentationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPresentacion;

    private String descripcion;

    private float precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private UserEntity usuario;

    @CreatedDate
    private Date fechaCreacion=new Date();

    @CreatedDate
    private LocalDateTime ultimaActualizacion=LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presentation", fetch = FetchType.LAZY)
//    @JsonManagedReference
    @JsonIgnore
    private List<PresentationMobiliarioEntity> presentationMobiliarioEntityList = new ArrayList<>();



    public PresentationEntity(CreatePresentationDTO presentationDTO, UserEntity actualUser) {
        this.descripcion = presentationDTO.description();
        this.precio = presentationDTO.precio();
        this.usuario = actualUser;
    }

    public void addPresentationMobiliarioEntity(PresentationMobiliarioEntity presentationMobiliarioEntity) {
        presentationMobiliarioEntity.setPresentation(this);
        this.presentationMobiliarioEntityList.add(presentationMobiliarioEntity);
    }

    public void remove(List<PresentationMobiliarioEntity> listaPresMobiliario) {
        this.presentationMobiliarioEntityList.remove(listaPresMobiliario);
    }
}
