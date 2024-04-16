package com.mobiliario.dto.clients;

import com.mobiliario.models.ClientsEntity;
import com.mobiliario.models.UserEntity;

public record DetalleCompletoClienteDTO(Long id,
                                        String nombre,
                                        String direccion,
                                        String telefono,
                                        UserEntity idUsuario,
                                        int active) {
    public DetalleCompletoClienteDTO(ClientsEntity client) {
        this(client.getIdCliente(), client.getNombre(), client.getDireccion(), client.getTelefono(),
                client.getUsuario(), client.getActive());
    }
}
