package com.mobiliario.controller;

import com.mobiliario.dto.mobiliario.CreateMobiliarioDTO;
import com.mobiliario.dto.mobiliario.DetalleMobiliarioCompletoDTO;
import com.mobiliario.dto.mobiliario.UpdateMobiliarioDTO;
import com.mobiliario.models.MobiliarioEntity;
import com.mobiliario.respositories.MobiliarioRepository;
import com.mobiliario.service.MobiliarioService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/mobiliario")
public class MobiliarioController {

    @Autowired
    private MobiliarioService mobiliarioService;

    @Autowired
    private MobiliarioRepository mobiliarioRepository;

    /**
     * Crea un nuevo registro de la entidad MobiliarioEntity (tabla mobiliario) con los datos recibidos
     * @param mobiliarioDTO Objeto encargado de recibir y representar la informacion que se utilizara para crear
     *                      el nuevo registro
     * @param request Objeto que recibe la informacion de la peticion del cliente
     * @param uriComponentsBuilder Objeto utilizado para crear la url del objeto que se acaba de crear
     * @return Regresa un objeto con la información del registro recien creado
     */
    @PostMapping
    @Transactional
    public ResponseEntity<DetalleMobiliarioCompletoDTO> addMobiliario(@RequestBody @Valid CreateMobiliarioDTO mobiliarioDTO,
                                           HttpServletRequest request,
                                           @org.jetbrains.annotations.NotNull UriComponentsBuilder uriComponentsBuilder){
        DetalleMobiliarioCompletoDTO detalleMobiliarioCompletoDTO =
                mobiliarioService.createMobiliario(mobiliarioDTO, request);
        URI url = uriComponentsBuilder.path("/mobiliario/{id}").buildAndExpand(detalleMobiliarioCompletoDTO.id()).toUri();
        return ResponseEntity.created(url).body(detalleMobiliarioCompletoDTO);
    }

    /**
     * Consulta todos los registros de la entidad MobiliarioEntity (tabla mobiliario) y crea un objeto de tipo
     * @param pageable dentro del cual se almacenaran los registros de la entidad de acuerdo a las especificaciones
     *                 indicadas en su definicion
     * @return Regresa un objeto de tipo Pageable con la información consultada de la entidad
     */
    @GetMapping
    public ResponseEntity<Page<DetalleMobiliarioCompletoDTO>> getAllMobiliario(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(mobiliarioRepository.findAll(pageable).map(DetalleMobiliarioCompletoDTO::new));
    }

    /**
     * Consulta un registro de la entidad MobiliarioEntity (tabla mobiliario) filtrando los resultados
     * con el parametro recibido a traves de la url
     * @param id objeto encargado de recibir y representar la llave del registro que se utilizara en la consulta
     * @return Objeto con la información del registro indicado en la url
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetalleMobiliarioCompletoDTO> getMobiliario(@PathVariable @NotNull Long id){
        MobiliarioEntity mobiliarioEntity = mobiliarioRepository.findById(id)
                .orElseThrow(()->new ValidationException("El id " + id + " no fue encontrado"));
        return ResponseEntity.ok(new DetalleMobiliarioCompletoDTO(mobiliarioEntity));
    }

    /**
     * Actualiza un registro de la entidad MobiliarioEntity (tabla mobiliario)
     * @param updateMobiliarioDTO Objeto encargado de recibir los datos de la peticion
     * @param request Objeto encargado de recibir la informacion adicional de la peticion
     * @return Objeto con la informacion del registro con la información actuailzada
     */
    @PutMapping
    public ResponseEntity<?> updateMobiliario(@RequestBody @Valid UpdateMobiliarioDTO updateMobiliarioDTO,
                                              HttpServletRequest request){
        DetalleMobiliarioCompletoDTO mobiliarioActualizado = mobiliarioService.update(updateMobiliarioDTO,request);
        return ResponseEntity.ok(mobiliarioActualizado);
    }

    /**
     * Elimina un registro utilizando como parametro de filtro el id recibido a traves de la url
     * @param id objeto encargado de recibir y representar el id del objeto
     * @return Mensaje de confirmacion de la eliminacion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ObjectNode> deleteMobiliario(@PathVariable @NotNull Long id){
        MobiliarioEntity mobiliarioEntity = mobiliarioRepository.findById(id)
                .orElseThrow(()->new ValidationException("El mobiliario con id "+ id + " no existe"));
        mobiliarioRepository.delete(mobiliarioEntity);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        jsonResponse.put("message","El mobiliario con id " + id + " fue eliminado con exito");
        return ResponseEntity.ok().body(jsonResponse);
    }
}
