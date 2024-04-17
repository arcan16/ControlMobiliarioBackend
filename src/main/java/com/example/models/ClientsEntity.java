package com.example.models;

import com.example.dto.clients.CreateClientDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ClientsEntity")
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;
    private String nombre;
    private String direccion;
    private String telefono;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private UserEntity usuario;

    private int active = 1;

    public ClientsEntity(CreateClientDTO dataClient, UserEntity userEntity) {
        this.nombre = dataClient.nombre();
        this.direccion = dataClient.direccion();
        this.telefono = dataClient.telefono();
        this.usuario = userEntity;
    }
}
