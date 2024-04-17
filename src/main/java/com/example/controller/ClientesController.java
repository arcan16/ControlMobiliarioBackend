package com.example.controller;

import com.example.dto.clients.ClienteDTO;
import com.example.dto.clients.CreateClientDTO;
import com.example.dto.clients.DetalleCompletoClienteDTO;
import com.example.dto.clients.UpdateClientDTO;
import com.example.models.ClientsEntity;
import com.example.respositories.ClientsRepository;
import com.example.service.ClientsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clients")
public class ClientesController {

    @Autowired
    private ClientsService clientService;

    @Autowired
    private ClientsRepository clientsRepository;

    /**
     * Crea un nuevo cliente
     * @param createClientDTO Informacion recibida con los campos necesarios para realizar el registro
     * @param request Informacion recibida de quien realiza la peticion
     * @param uriComponentsBuilder Crea la url del registro creado para poder ser consultado o modificado posteriormente
     * @return Objeto con la confirmacion sobre el registro realizado
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    public ResponseEntity<DetalleCompletoClienteDTO> addCustomer(@RequestBody @Valid CreateClientDTO createClientDTO,
                                         HttpServletRequest request,
                                         UriComponentsBuilder uriComponentsBuilder){
        ClientsEntity client = clientService.createClient(createClientDTO,request);
        URI url = uriComponentsBuilder.path("/clients/{id}").buildAndExpand(client.getIdCliente()).toUri();
        DetalleCompletoClienteDTO detalleCompletoClienteDTO = new DetalleCompletoClienteDTO(client);
        return ResponseEntity.created(url).body(detalleCompletoClienteDTO);
    }

    /**
     * Consulta en tabla clientes el registro que coincida con el nombre recibido
     * @param clienteDTO Informacion recibida de quien consume la api con el nombre que se desea buscar
     * @return Objeto con la informacion del cliente encontraada
     */
    @PostMapping("/name")
    public ResponseEntity<DetalleCompletoClienteDTO> getClienteByName(@RequestBody @Valid ClienteDTO clienteDTO){
        ClientsEntity client = clientService.getClientByName(clienteDTO);
        return ResponseEntity.ok(new DetalleCompletoClienteDTO(client));
    }

    /**
     * Realiza un consulta de todos los clientes registrados en el banco de datos
     * @param pageable Define la cantidad maxima de elementos que contendra la pagina creada
     * @return Regresa un dto con los elementos encontrados
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Page<DetalleCompletoClienteDTO>> getAllClients(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(clientsRepository.findAll(pageable).map(DetalleCompletoClienteDTO::new));
    }

    /**
     * Permite leer la informacion de un registro de la entidad ClientsEntity (tabla clientes) utilizando
     * la clave id como parametro de filtro
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<DetalleCompletoClienteDTO> getClient(@PathVariable @NotNull Long id){
        clientService.exists(id);
        ClientsEntity client = clientsRepository.findById(id).orElseThrow(()->new ValidationException("Error"));

        return ResponseEntity.ok(new DetalleCompletoClienteDTO(client));
    }

    /**
     * Permite actualizar los datos del cliente
     * @param clientDTO
     * @param request
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> updateClient(@RequestBody @Valid UpdateClientDTO clientDTO,
                                          HttpServletRequest request){

        return ResponseEntity.ok(clientService.update(clientDTO, request));
    }


    /**
     * Elimina un registro de la entidad ClientsEntity (tabla clientes)
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> deleteClient(@PathVariable @NotNull Long id){
        clientService.exists(id);
        ClientsEntity client = clientsRepository.getReferenceById(id);
        clientsRepository.delete(client);

        return ResponseEntity.ok("Registro con ID "+id+" eliminado correctamente");
    }

    /**
     * Elimina el cliente junto con las reservaciones que haya realizado siempre y cuando las reservaciones no hayan sido
     * entregadas o no se encuentren vigentes (es decir, presetadas)
     * @param id
     * @return
     */
    @DeleteMapping("/all/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> deleteAllClient(@PathVariable @NotNull Long id){
        clientService.exists(id);

        ObjectNode jsonResponse= clientService.deleteAll(id);


        return ResponseEntity.ok().body(jsonResponse);
    }


    /**
     * Cambia el es estado de la columna active para que pueda o no ser utilizado este cliente para agendar
     * reservaciones de mobiliario
     * @param id Parametro de tipo integer con la clave primaria del registro que sera modificado
     * @return
     */
    @DeleteMapping("/active/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> statusClient(@PathVariable @NotNull Long id){
        clientService.exists(id);
        ClientsEntity client = clientsRepository.getReferenceById(id);
        if(client.getActive()==0){
            client.setActive(1);
        }else{
            client.setActive(0);
        }
        clientsRepository.save(client);
//        clientsRepository.delete(client);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        jsonResponse.put("message","Registro con ID "+id+" 'eliminado' correctamente");
        return ResponseEntity.ok().body(jsonResponse);
    }


}
