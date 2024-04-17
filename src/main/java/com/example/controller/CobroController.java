package com.example.controller;

import com.example.dto.cobro.*;
import com.example.models.CobroEntity;
import com.example.models.ReservacionEntity;
import com.example.service.CobroService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cobros")
public class CobroController {

    @Autowired
    private CobroService cobroService;

    /**
     * Crea un registro con el cobro realizado para la reservacion
     * @param idReservacion Llave primaria utilizada para totalizar el cobro
     * @param request Informacion adicional de la peticion
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addCobro(@RequestBody @Valid CreateCobroDTO idReservacion,
                                      HttpServletRequest request,
                                      UriComponentsBuilder uriComponentsBuilder){
        ReservacionEntity reservacion = cobroService.createCobro(idReservacion, request);
        URI url = uriComponentsBuilder.path("/total/{id}").buildAndExpand(reservacion.getIdReservacion()).toUri();
        return ResponseEntity.created (url).body(reservacion);
    }

    /**
     * Genera un objeto de tipo Page con todos los registros de la entidad CobroEntity (tabla cobro)
     * @param pageable Define caracteristicas como el tamaño de la página
     * @return Objeto con una pagina que contiene los registros encontrados en la entidad
     */
    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 100)Pageable pageable){
        return ResponseEntity.ok(cobroService.getAll(pageable).map(GetCobroDTO::new));
    }

    /**
     * Regresa la informacion completa de un registro de la entidad ReservacionEntity (tabla reservacion)
     * @param idReservacion
     * @return
     * el parametro "valido" indica si un registro esta activo (1) o cancelado (0)
     */
    @GetMapping("/total/{idReservacion}")
    public ResponseEntity<DetalleCobroDTO> getTotal(@PathVariable @NotNull Long idReservacion){
        DetalleCobroDTO detalleCobroDTO = cobroService.getTotal(idReservacion);
        return ResponseEntity.ok(detalleCobroDTO);
    }

    /**
     * Consulta los cobros validos almacenados en la entidad CobroEntity (tabla cobro)
     * @param pageable Define caracteristicas como el tamaño de la página
     * @return Objeto con los registros validos de la entidad
     */
    @GetMapping("/valid")
    public ResponseEntity<Page<GetCobroDTO>> getValid(@PageableDefault(size = 10)Pageable pageable){

        return ResponseEntity.ok(cobroService.valid(pageable).map(GetCobroDTO::new));
    }
    /**
     * Consulta los cobros cancelados almacenados en la entidad CobroEntity (tabla cobro)
     * @param pageable Define caracteristicas como el tamaño de la página
     * @return Objeto con los registros cancelados de la entidad
     */
    @GetMapping("/canceled")
    public ResponseEntity<Page<GetCobroDTO>> getCanceled(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(cobroService.inValid(pageable).map(GetCobroDTO::new));
    }

    /**
     * Calcula el total de los cobros realizados en un dia determinado
     * @param fecha Parametro recibido a traves de la url con el que se filtraran los registros para calcular el total
     * @return Mensaje con el total de cobros realizados
     */
    @GetMapping("/total")
    public ResponseEntity<?> totalDiario(@RequestParam(value = "fecha",required = true)
                                             @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDateTime fecha
                                             ){

        double total = cobroService.getTotalFecha(fecha);
        return ResponseEntity.ok("El total de cobro de la fecha "+fecha+" es de "+total);
    }

    // Vamos a generar el total de cobros hechos dentro de un periodo de tiempo determinado

    /**
     * Generar el total de cobros hechos dentro de un periodo de tiempo determinado
     * @param totalPeriodoDTO Periodo a consultar
     * @return Total de cobros realizados dentro de un periodo determinado
     */
    @GetMapping("/periodo")
    public ResponseEntity<?> getTotalPeriodo(@RequestBody @Valid TotalPeriodoDTO totalPeriodoDTO){
        double total = cobroService.totalPerodo(totalPeriodoDTO);
        return ResponseEntity.ok(total);
    }

    /**
     * Consulta los cobros de una fecha determinada
     * @param requestBody
     * @return
     */
    @PostMapping("/fecha")
    public ResponseEntity<?> getTotalFecha(@RequestBody Map<String, String> requestBody){
        String fechaString = requestBody.get("fecha");
        LocalDateTime fecha = LocalDateTime.parse(fechaString);
        System.out.println("Fecha: " + fecha);
        List<CobroEntity> cobros = cobroService.findByFecha(fecha);
        return ResponseEntity.ok(cobros.stream().map(GetCobroDTO::new));
    }

    /**
     * Permite actualizar el total de cobro de un registro, unicamente para usuarios de tipo ADMIN
     * @param updateCobroDTO Datos recibidos del usuario
     * @param request Informacion recibida de la peticion
     * @return Objeto de tipo UpdateCobroDTO con la informacion actualizada del registro
     */
    @PutMapping
    public ResponseEntity<UpdateCobroDTO> updateCobro(@RequestBody @Valid UpdateCobroDTO updateCobroDTO,
                                         HttpServletRequest request){
        UpdateCobroDTO cobroActualizado = cobroService.update(updateCobroDTO,request);
        return ResponseEntity.ok(cobroActualizado);
    }

    /**
     * Cancela un registro de la entidad CobroEntity (tabla cobro)
     * @param idCobro Llave primaria utilizada para buscar y actualizar el registro
     * @param request Informacion sobre la peticion recibida
     * @return Mensaje de confirmacion
     */
    @DeleteMapping("/cancel/{idCobro}")
    public ResponseEntity<?> cancelCobro(@PathVariable @NotNull Long idCobro, HttpServletRequest request){
        cobroService.cancel(idCobro, request);
        return ResponseEntity.ok("El cobro con id: "+ idCobro + " ha sico cancelado");
    }

    /**
     * Elimina un registro de la entidad CobroEntity (tabla cobro) utilizando el parametro recibido por url
     * @param idCobro id del registro que se desea eliminar
     * @param request Informacion de la peticion que se utilizara para obtener el usuario y sus privilegios
     * @return Mensaje de confirmacion sobre la eliminacion del registro
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCobro(@PathVariable @NotNull Long idCobro,
                                         HttpServletRequest request){
        cobroService.delete(idCobro, request);
        return ResponseEntity.ok("El registro con id: " + idCobro + " ha sido eliminado exitosamente");
    }
}
