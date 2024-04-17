package com.example.controller;

import com.example.dto.mobiliario.tipo.CreateTipoMobiliarioDTO;
import com.example.dto.mobiliario.tipo.DetalleCompletoTipoMobiliarioDTO;
import com.example.dto.mobiliario.tipo.DetalleTipoMobiliarioDTO;
import com.example.dto.mobiliario.tipo.UpdateTipoMobiliario;
import com.example.models.TipoMobiliarioEntity;
import com.example.models.UserEntity;
import com.example.respositories.TipoMobiliarioRepository;
import com.example.service.users.UsuarioService;
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
@RequestMapping("/tipoMobiliario")
public class TipoMobiliarioController {

    @Autowired
    private TipoMobiliarioRepository tipoMobiliarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Crea un registro a partir de la informacion recibida dentro del post de la peticion
     * @param createTipoMobiliarioDTO Objeto encargado de recibir la informacion
     * @param request Datos de la peticion que se recibe
     * @param uriComponentsBuilder Objeto encargado de la creacion de una url para la futura consulta del registro creado
     * @return Regresa un objeto con la informacion del registro creado
     */
    @PostMapping
    @Transactional
    public ResponseEntity<?> addTipoMobiliario(@RequestBody @Valid CreateTipoMobiliarioDTO createTipoMobiliarioDTO,
                                               HttpServletRequest request,
                                               UriComponentsBuilder uriComponentsBuilder){
        if(tipoMobiliarioRepository.existsByNombre(createTipoMobiliarioDTO.nombre())){
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonResponse = objectMapper.createObjectNode();

            jsonResponse.put("message","El tipo de mobiliario ya existe");
            jsonResponse.put("statusText","El tipo de mobiliario ya existe");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        UserEntity userEntity = usuarioService.getActualUser(request);

        TipoMobiliarioEntity tipoMobiliario = tipoMobiliarioRepository.save(new TipoMobiliarioEntity(createTipoMobiliarioDTO, userEntity));

        URI url = uriComponentsBuilder.path("/tipoMobiliario/{id}").buildAndExpand(tipoMobiliario.getIdTipo()).toUri();

        return ResponseEntity.created(url).body(tipoMobiliario);
    }

    /**
     * Genera una pagina con los registros recuperados de la entidad TipoMobiliariEntity (tabla tipo_mobiliario)
     * @param pageable Objeto encargado de definir las caracteristicas de la pagina que sera creada con todos los registros
     *                 recuperados de la entidad
     * @return Regresa un objeto de tipo page con los registros consultados
     */
    @GetMapping
    public ResponseEntity<Page<DetalleCompletoTipoMobiliarioDTO>> getAllTipoMobiliario(@PageableDefault(size = 30)Pageable pageable){
        return ResponseEntity.ok(tipoMobiliarioRepository.findAll(pageable).map(DetalleCompletoTipoMobiliarioDTO::new));
    }

    /**
     * Permite consultar la informacion de un registro en particular utilizando el id recibido en la peticion
     * @param id Identificador del registro que sera utilizado para la consulta
     * @return Regresa un objeto con la informacion encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTipoMobiliario(@PathVariable @NotNull Long id){
        TipoMobiliarioEntity tipoMobiliario = tipoMobiliarioRepository.findById(id).
                orElseThrow(()->new ValidationException("El id "+ id +" no existe"));
        DetalleTipoMobiliarioDTO detalleTipoMobiliarioDTO = new DetalleTipoMobiliarioDTO(tipoMobiliario);
        return ResponseEntity.ok(detalleTipoMobiliarioDTO);
    }

    /**
     * Permite la actualizacion de informacion de un registro
     * @param updateTipoMobiliario Datos recibidos que seran utilizados para actualizar la inforamcion del registro
     * @param request Datos del usuario que ha realizado la peticion
     * @return Retorna la informacion de los registros actualizada
     */
    @PutMapping
    public ResponseEntity<?> updateTipoMobiliario(@RequestBody @Valid UpdateTipoMobiliario updateTipoMobiliario,
                                                  HttpServletRequest request){
        TipoMobiliarioEntity tipoMobiliario = tipoMobiliarioRepository.findById(updateTipoMobiliario.id())
                .orElseThrow(()-> new ValidationException("El id no coincide con ningun registro"));

        if(updateTipoMobiliario.nombre()!=null && !updateTipoMobiliario.nombre().isBlank()){
            tipoMobiliario.setNombre(updateTipoMobiliario.nombre());
            UserEntity usuario = usuarioService.getActualUser(request);
            tipoMobiliario.setUsuario(usuario);
        }
        DetalleTipoMobiliarioDTO detalleTipoMobiliarioDTO =
                new DetalleTipoMobiliarioDTO(tipoMobiliarioRepository.save(tipoMobiliario));
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("message","Registro actualizado correctamente");
        return ResponseEntity.ok().body(jsonResponse);
    }

    /**
     * Elimina un registro de la entidad TipoMobiliarioEntity (tabla tipo_mobiliario) utilizando el id recibido en la url
     * @param id identificador del registro que sera utilizado para eliminacion
     * @return Mensaje de confirmacion de eliminacion
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteTipoMobiliario(@PathVariable @NotNull Long id){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        if(!tipoMobiliarioRepository.existsById(id)){
            jsonResponse.put("statusText","El id proporcionado no coincide con ningun elemento almacenado");
            return ResponseEntity.badRequest().body(jsonResponse);
//            throw new ValidationException("El id proporcionado no coincide con ningun elemento almacenado");
        }
        tipoMobiliarioRepository.deleteById(id);
        jsonResponse.put("message","El registro con id: "+id+" ha sido eliminado con exito");
        return ResponseEntity.ok().body(jsonResponse);
    }
}