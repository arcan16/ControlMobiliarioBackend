package com.mobiliario.service;

import com.mobiliario.dto.reservacion.*;
import com.mobiliario.dto.reservacion.validations.ReservaValidation;
import com.mobiliario.dto.reservacion.validations.StockDisponibleValidation;
import com.mobiliario.models.*;
import com.mobiliario.respositories.*;
import com.mobiliario.service.users.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ReservacionService {

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private ReservacionRepository reservacionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PresentationRepository presentationRepository;

    @Autowired
    private ReservaMobiliarioRepository reservaMobiliarioRepository;

    @Autowired
    private List<ReservaValidation> validations = new ArrayList<>();

    @Autowired
    private StockDisponibleValidation stockDisponibleValidation;

    @Transactional
    public void create(@Valid CreateReservacionDTO reservacionDTO, HttpServletRequest request){
        ClientsEntity cliente = clientsRepository.findById(reservacionDTO.idCliente())
                .orElseThrow(()->new ValidationException("El cliente indicado no existe"));
        UserEntity actualUser = usuarioService.getActualUser(request);

        /*  VALIDACIONES:
            fecha mayor a la actual
         */
        ReservacionEntity reservacion = new ReservacionEntity(cliente,actualUser,reservacionDTO);

        reservacionRepository.save(reservacion);
        List<ReservaMobiliarioEntity> reservaMobiliarioEntityList =
                reservacionDTO.reservPrestamoList().stream().map(elemento->{
                    /*  VALIDACIONES
                      - El mobiliario solicitado debera estar disponible para la fecha indicada
                      - La cantidad debe ser positiva
                     */
                    validations.forEach(v->v.Validate(elemento,reservacion));

                    PresentationEntity presentation = presentationRepository.findById(elemento.idPresentacion())
                            .orElseThrow(()->new ValidationException("La presentacion con id: "+ elemento.idPresentacion()
                                    + " no existe"));

                    ReservaMobiliarioEntityId id = new ReservaMobiliarioEntityId();
                    id.setIdReservacion(reservacion.getIdReservacion());
                    id.setIdPresentacion(presentation.getIdPresentacion());

                    ReservaMobiliarioEntity reservaMobiliario = new ReservaMobiliarioEntity();
                    reservaMobiliario.setId(id);

                    reservaMobiliario.setReservacion(reservacion);
                    reservaMobiliario.setPresentation(presentation);
                    reservaMobiliario.setCantidad(elemento.cantidad());

                    return reservaMobiliario;
                }).toList();
        reservacion.setReservaMobiliarioList(reservaMobiliarioEntityList);
    }

    public void delete(Long id) {
        exists(id);
        reservacionRepository.deleteById(id);
    }

    public boolean exists(Long id){
        if(!reservacionRepository.existsById(id)){
            throw new ValidationException("El id proporcionado no existe");
        }
        return true;
    }

    @Transactional
    public void update(UpdateReservacionDTO updateReservacionDTO, HttpServletRequest request) {
        ReservacionEntity reservacion = reservacionRepository.findById(updateReservacionDTO.idReservacion())
                .orElseThrow(()->new ValidationException("El id proporcionado no existe"));

        UserEntity usuarioActual = usuarioService.getActualUser(request);
        reservacion.setUsuario(usuarioActual);
        if(updateReservacionDTO.idCliente()!=null){
            ClientsEntity clients = clientsRepository.findById(updateReservacionDTO.idCliente())
                            .orElseThrow(()->new ValidationException(""));
            reservacion.setCliente(clients);
        }
        if(updateReservacionDTO.direccionEntrega()!=null && !updateReservacionDTO.direccionEntrega().isBlank()){
            reservacion.setDireccionEntrega(updateReservacionDTO.direccionEntrega());
        }
        if(updateReservacionDTO.fechaEntrega()!=null &&(!updateReservacionDTO.fechaEntrega().isEqual(reservacion.getFechaEntrega()))){
            reservacion.setFechaEntrega(updateReservacionDTO.fechaEntrega());
        }
        if(updateReservacionDTO.fechaRecepcion()!=null){
            reservacion.setFechaRecepcion(updateReservacionDTO.fechaRecepcion());
        }
        /*
            Con esta linea podremos actuailzar el estado de la reservacion:
            - 0 No entregado
            - 1 Entregado
            - 2 Cobrado
            - 3 Cancelado
         */
        if(updateReservacionDTO.status()!=null && updateReservacionDTO.status()>=0){
            reservacion.setStatus(updateReservacionDTO.status());
        }

        // Actualizacion de la lista de elementos del registro - updateReservacionDTO es lo que llego
        if(updateReservacionDTO.reservPrestamoList()!=null){
            for(ReservaPrestamoDTO reservaPrestamoDTO : updateReservacionDTO.reservPrestamoList()){
                int existeRegistro = 0;
                for(ReservaMobiliarioEntity reservaMobiliario: reservacion.getReservaMobiliarioList()){
                    if (reservaPrestamoDTO.idPresentacion().equals(reservaMobiliario.getPresentation().getIdPresentacion())){
                        reservaMobiliario.setCantidad(reservaPrestamoDTO.cantidad());
                        existeRegistro=1;
                        break;
                    }
                }
                if(existeRegistro==0){
                    PresentationEntity presentation = presentationRepository.findById(reservaPrestamoDTO.idPresentacion())
                            .orElseThrow(()->new ValidationException("La presentacion con id "
                                    + reservaPrestamoDTO.idPresentacion() + " no existe"));

                    ReservaMobiliarioEntity reservaMobiliario = new ReservaMobiliarioEntity();
                    ReservaMobiliarioEntityId id = new ReservaMobiliarioEntityId();
                    id.setIdReservacion(reservacion.getIdReservacion());
                    id.setIdPresentacion(reservaPrestamoDTO.idPresentacion());
                    reservaMobiliario.setId(id);
                    reservaMobiliario.setCantidad(reservaPrestamoDTO.cantidad());
                    reservaMobiliario.setPresentation(presentation);
                    reservaMobiliario.setReservacion(reservacion);
                    reservacion.getReservaMobiliarioList().add(reservaMobiliario);
                }
            }

//        System.out.println("Test de cantidad "+reservacion.getReservaMobiliarioList().stream().map(ReservaPrestamoDTO::new));
            reservacion.getReservaMobiliarioList().forEach(el-> System.out.println(el.getId() + " " + el.getCantidad()));

            stockDisponibleValidation.Validate(reservacion);
        }

    }

    public void deleteReservaMobiliario(DeleteReservaMobiliarioDTO deleteReservaMobiliarioDTO) {
        ReservacionEntity reservacion = reservacionRepository.findById(deleteReservaMobiliarioDTO.idReservacion())
                .orElseThrow(()->new ValidationException("El id de la reservacion no existe"));
        presentationRepository.findById(deleteReservaMobiliarioDTO.idPresentacion())
                .orElseThrow(()->new ValidationException("El id de la presentacion no existe"));

        Iterator<ReservaMobiliarioEntity> iterator = reservacion.getReservaMobiliarioList().iterator();

        while(iterator.hasNext()){
            ReservaMobiliarioEntity reservaMobiliario = iterator.next();
            if(reservaMobiliario.getPresentation().getIdPresentacion().equals(deleteReservaMobiliarioDTO.idPresentacion())){
                iterator.remove();

                reservaMobiliarioRepository.delete(reservaMobiliario);
            }
        }

        reservacionRepository.save(reservacion);
    }

    public Page<ReservacionEntity> reservacionesActivas(Pageable pageable) {
        return reservacionRepository.findAllByStatusActive(pageable);
    }

    public Page<ReservacionEntity> prestamosVigentes(Pageable pageable) {
        return reservacionRepository.prestamosVigentes2(pageable);
    }

    public Page<ReservacionEntity> entregasDelDia(Pageable pageable) {
        return reservacionRepository.getEntregasDeldia(pageable);
    }

    public Page<ReservacionEntity> recepcionDelDia(Pageable pageable) {
        return reservacionRepository.getRecepcionDeldia(pageable);
    }

//    public Page<ReservacionEntity> getPeriodo(PeriodoDTO periodoDTO, Pageable pageable) {
//        return reservacionRepository.getPeriodo(pageable, periodoDTO.fecha1());
//    }

    public Page<ReservacionEntity> getPeriodo(PeriodoDTO periodoDTO, Pageable pageable) {
        return reservacionRepository.getPeriodo(pageable,periodoDTO.fecha1(), periodoDTO.fecha2());
    }
}
