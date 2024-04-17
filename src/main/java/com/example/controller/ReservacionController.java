package com.example.controller;

import com.example.dto.reservacion.*;
import com.example.models.ReservacionEntity;
import com.example.respositories.ReservacionRepository;
import com.example.service.ReservacionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservacion")
public class ReservacionController {

    @Autowired
    private ReservacionService reservacionService;

    @Autowired
    private ReservacionRepository reservacionRepository;

    /**
     * Creaa una nueva reservacion
     * @param reservacionDTO Datos recibidos en la peticion
     * @param request Datos que forman parte de la peticion
     * @return Confirmacion de proceso correcto
     */
    @PostMapping
    public ResponseEntity<?> addReservacion(@RequestBody @Valid CreateReservacionDTO reservacionDTO,
                                            HttpServletRequest request){
        System.out.println(reservacionDTO);
        reservacionService.create(reservacionDTO,request);
        return ResponseEntity.ok().build();
    }

    /**
     * Consulta los registros que se encuentren dentro de un periodo especifico de tiempo
     * La consulta sera para todos los registros en este periodo de tiempo incluyendo entregas y recepciones
     * dentro de estas fechas
     * @param periodoDTO
     * @return
     */
    @PostMapping("/periodo")
    public ResponseEntity<?> getReservacionesPeriodo(@RequestBody @Valid PeriodoDTO periodoDTO,
                                                     @PageableDefault(size = 10)Pageable pageable){
//        System.out.println(periodoDTO);
//        reservacionService.getPeriodo(periodoDTO,pageable);
        return ResponseEntity.ok(reservacionService.getPeriodo(periodoDTO, pageable).map(DetalleReservacionDTO::new));
//        return ResponseEntity.ok().build();
    }

    /**
     * Consulta todos los registros de la tabla reservaciones
     * @param pageable Define la cantidad de elementos que contendra el objeto
     * @return Objeto con la informaacion encontrada en los registros
     */
    @GetMapping
    public ResponseEntity<Page<DetalleReservacionDTO>> getReservaciones(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(reservacionRepository.findAll(pageable).map(DetalleReservacionDTO::new));
    }

    /**
     * Busca en la tabla el registro que coincida con el id recibido
     * @param id parametro recibido en la url de la peticion
     * @return Informacion que coincide con el parametro recibido
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservacion(@PathVariable @NotNull Long id){
        ReservacionEntity reservacion = reservacionRepository.findById(id)
                .orElseThrow(()->new ValidationException("El id proporcionado no existe"));
        return ResponseEntity.ok(new DetalleReservacionDTO(reservacion));
    }

    /**
     * Consulta los registros de la entidad ReservacionEntity (tabla reservacion) con estatus 1 - entregdo
     * @param pageable Define la cantidad de registros que tendra el objeto Pageable de respuesta
     * @return Objeto tipo Pageable con los resultados encontrados
     */
    @GetMapping("/statusOne")
    public ResponseEntity<?> getReservacionesActivas(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(reservacionService.reservacionesActivas(pageable).map(DetalleReservacionDTO::new));
    }

    /**
     * Consulta los registros de las reservaciones que aun no se han entregado y tampoco han sido canceladas
     * @param pageable Define el tamaño del objeto que sera creado dentro del metodo
     * @return Objeto de tipo Pageable<DetalleReservacionDTO> con el resultado de la consulta
     */
    @GetMapping("/vigentes")
    public ResponseEntity<?> prestamosVigentes(@PageableDefault(size = 10, sort = "fechaEntrega" ) Pageable pageable){
        return ResponseEntity.ok(reservacionService.prestamosVigentes(pageable).map(DetalleReservacionDTO::new));
    }

    /**
     * Genera un objeto con los registros de la entidad ReservacionEntity cuya fecha corresponda al dia de hoy
     * @param pageable Define el tamaño del objeto que sera creado dentro del metodo
     * @return Objeto con los registros de las reservaciones que deberan entregarse el dia de hoy
     */
    @GetMapping("/entregas")
    public ResponseEntity<?> getEntregasDelDia(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(reservacionService.entregasDelDia(pageable).map((DetalleReservacionDTO::new)));
    }

    /**
     * Genera un objeto con los registros de la entidad ReservacionEntity cuya fecha corresponda al dia de hoy
     * @param pageable Define el tamaño del objeto que sera creado dentro del metodo
     * @return Objeto con los registros de las reservaciones que deberan entregarse el dia de hoy
     */
    @GetMapping("/recepcion")
    public ResponseEntity<?> getRecepcionDelDia(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(reservacionService.recepcionDelDia(pageable).map((DetalleReservacionDTO::new)));
    }

    /**
     * Actualiza un registro de la tabla
     * @param updateReservacionDTO Datos recibidos en la peticion que seran utilizados para la actualizacion
     * @param request Datos recibidos dentro de la peticion para procesos internos de validacion
     * @return Mensaje de confirmacion
     */
    @PutMapping
    public ResponseEntity<?> updateReservacion(@RequestBody @Valid UpdateReservacionDTO updateReservacionDTO,
                                               HttpServletRequest request){
        reservacionService.update(updateReservacionDTO, request);
        return ResponseEntity.ok().build();
    }

    /**
     * Elimina un registro de la tabla con la informacion recibida en la peticion
     * @param deleteReservaMobiliarioDTO Datos recibidos en la peticion para ejecutar la eliminacion
     * @return Mensaje de confirmacion
     */
    @DeleteMapping
    public ResponseEntity<?> deleteReservaMobiliario(@RequestBody @Valid
                                                         DeleteReservaMobiliarioDTO deleteReservaMobiliarioDTO){
        reservacionService.deleteReservaMobiliario(deleteReservaMobiliarioDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Elimina un registro de la entidad ReservacionEntity (tabla reservacion) y los registros que contengan
     * el mismo id_preserntacion de la entidad ReservaMobiliarioEntity (tabla reserva_mobiliario) en cascada
     * @param id Parametro recibido a traves de la url para la transaccion
     * @return Mensaje de confirmacion con la eliminacion del registro
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservacion(@PathVariable @NotNull Long id){
        reservacionService.delete(id);
        return ResponseEntity.ok("El registro con id "+id+" fue eliminado correctamente");
    }
}
