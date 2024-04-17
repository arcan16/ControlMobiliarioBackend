package com.example.controller;

import com.example.dto.presentation.*;
import com.example.respositories.PresentationRepository;
import com.example.service.PresentationService;
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

@RestController
@RequestMapping("/presentation")
public class PresentationController {

    @Autowired
    private PresentationService presentationService;

    @Autowired
    private PresentationRepository presentationRepository;

    /**
     * Crea una presentacion nueva
     * @param presentationDTO Datos recibidos en la peticion para la creaacion de la peticion
     * @param request Datos de la peticion utilizados para procesos internos
     * @return Mensaje de confirmacion con la creaacion del registro
     */
    @PostMapping
    @Transactional
    public ResponseEntity<?> addPresentation(@RequestBody @Valid CreatePresentationDTO presentationDTO,
                                             HttpServletRequest request){
        presentationService.create(presentationDTO, request);
        return ResponseEntity.ok(presentationDTO);
    }

    /**
     * Recupera todos los registros de la entidad PresentationEntity (tabla presentacion) y los registros
     * enlazados a la entidad PresentationMobiliarioEntity (tabla presentacion_mobiliario) utilizando
     * un parametro de paginacion para agruparlos
     * @param pageable Determina el tamaño del objeto que contendra los registros
     * @return Regresa un objeto de tipo Page con la informacion imbuida en un DTO para evitar recursividad
     */
    @GetMapping
    public ResponseEntity<Page<PresentacionDTO>> getAllPresentations(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(presentationRepository.findAll(pageable).map(PresentacionDTO::new));
    }

    /**
     * Recupera un registro de la entidad PresentationEntity (tabla presentacion) y los registros enlazados
     * a la entidad PresentationMobiliarioEntity (tabla presentacion_mobiliario) utilizando el id recibido
     * a traves de la url como parametro de busqueda
     * @param id Llave que sera utilizada para la consulta
     * @return Objeto DTO encargado de representar la informacion encontrada en la entidad
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPresentation(@PathVariable @NotNull Long id){
        PresentacionDTO presentacion = presentationRepository.findById(id).map(PresentacionDTO::new)
                .orElseThrow(()->new ValidationException("El id proporcionado no fue encontrado"));
        System.out.println(presentationRepository.findById(id).map(PresentacionDTO::new)
                .orElseThrow(()->new ValidationException("El id proporcionado no fue encontrado")));
//        PresentacionDTO presentacion = new PresentacionDTO(presentationRepository.getReferenceById(id));
        return ResponseEntity.ok(presentacion);
    }

    /**
     * Actualiza la informacion de un registro de la entidad PresentationEntity (tabla presentacion) y
     * los registros relacionados a la entidad PresentationMobiliarioEntity (tabla presentacion_mobiliario)
     * agregando registros al encontrar informacion nueva
     * @param updatePresentationDTO Objeto creado con los datos recibidos que seran actualizados
     * @param request Objeteo con la informacion de la peticion
     * @return Regresa un objeto de tipo presentacionDTO con la informacion del registro actualizada
     */
    @PutMapping
    public ResponseEntity<?> updatePresentation(@RequestBody @Valid UpdatePresentationDTO updatePresentationDTO,
                                                HttpServletRequest request){
        PresentacionDTO presentacionDTO = presentationService.update(updatePresentationDTO,request);
        return ResponseEntity.ok(presentacionDTO);
    }

    /**
     * Actualiza la informacion del registro
     * @param updatePresentationDTO
     * @param request
     * @return
     */
    @PutMapping("/all")
    public ResponseEntity<?> completeUPdatePresentation(@RequestBody @Valid UpdatePresentationDTO updatePresentationDTO,
                                                   HttpServletRequest request){
        PresentacionDTO presentacionDTO = presentationService.completeUpdate(updatePresentationDTO, request);
        return ResponseEntity.ok(presentacionDTO);
    }

    /**
     * Elimina un registro de la entidad PresentationEntity (tabla presentacion) y elimina en cascada los
     * registros relacionados a la entidad PresentationMobiliarioEntity (tabla presentacion_mobiliario)
     * utilizando el id como parametro de busqueda
     * @param id Parametro recibido  a traves de la url que sera utilizado para la consulta
     * @return String con confirmacion de eliminacion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePresentation(@PathVariable @NotNull Long id){
        presentationRepository.findById(id).orElseThrow(()->new ValidationException("El registro" +
                " con el id proporcionado no existe"));
        presentationRepository.deleteById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        jsonResponse.put("message","La presentacion con id " + id + " fue eliminado con exito");
        return ResponseEntity.ok().body(jsonResponse);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePresentationMobiliario(@RequestBody DeletePresentationMobiliario deletePresentationMobiliario){
        presentationService.deletePresentationMobiliario(deletePresentationMobiliario);

        return ResponseEntity.ok().build();
    }
}
