package com.mobiliario.service;

import com.mobiliario.dto.cobro.CreateCobroDTO;
import com.mobiliario.dto.cobro.DetalleCobroDTO;
import com.mobiliario.dto.cobro.TotalPeriodoDTO;
import com.mobiliario.dto.cobro.UpdateCobroDTO;
import com.mobiliario.models.*;
import com.mobiliario.respositories.CobroRepository;
import com.mobiliario.respositories.ReservacionRepository;
import com.mobiliario.service.users.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CobroService {

    @Autowired
    private CobroRepository cobroRepository;

    @Autowired
    private ReservacionRepository reservacionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public ReservacionEntity createCobro(CreateCobroDTO reservacionDto, HttpServletRequest request){

        ReservacionEntity reservacion = exists(reservacionDto.idReservacion());

        if(reservacion.getStatus()==2){
            throw new ValidationException("La reservacion ya ha sido cobrada");
        }

        UserEntity actualUser = usuarioService.getActualUser(request);

        CobroEntity cobro = new CobroEntity();

        float total = getTotalRenta(reservacion);

        System.out.println("El total de la renta de mobiliario es de: $" + total);
        System.out.println("El usuario actual tiene rol " + actualUser.getRol().getRol());

        if(total != reservacionDto.total()){
            if(actualUser.getRol().getRol()== ERole.ADMIN){
                cobro.setTotal(reservacionDto.total());
            }else{
                throw new ValidationException("Solo un usuario ADMIN puede dar descuentos");
            }
        }else{
            cobro.setTotal(total);
        }

        cobro.setReservacion(reservacion);
        cobro.setValido(true);
        cobro.setUsuario(actualUser);

        cobroRepository.save(cobro);
        reservacion.setStatus(2);
        return reservacionRepository.save(reservacion);
    }

    public ReservacionEntity exists(Long idReservacion){
        return reservacionRepository.findById(idReservacion)
                .orElseThrow(()-> new ValidationException("El id de la reservacion que proporcionaste no existe"));
    }

    public DetalleCobroDTO getTotal(Long idReservacion){
        ReservacionEntity reservacion = exists(idReservacion);
        float total = getTotalRenta(reservacion);

        return new DetalleCobroDTO(reservacion,total);
    }

    public double getTotalFecha(LocalDateTime fecha){
        List<ReservacionEntity> reservacionesList = reservacionRepository.findAllByFechaRecepcion(fecha);
        reservacionesList.forEach(el-> System.out.println(el.getIdReservacion()));
        double total = 0;
        total = reservacionesList.stream().mapToDouble(el->getTotal(el.getIdReservacion()).total()).sum();
//        System.out.println("El total de cobros de la fecha: "+fecha+" es de: "+ total);
        return total;
    }

    public float getTotalRenta(ReservacionEntity reservacion){
        long diasPrestamo = Duration.between(reservacion.getFechaEntrega(), reservacion.getFechaRecepcion()).toDays();
        float totalMobiliario=0;
        Iterator<ReservaMobiliarioEntity> iterator= reservacion.getReservaMobiliarioList().iterator();
        while(iterator.hasNext()){
            ReservaMobiliarioEntity reservaMobiliario = iterator.next();
            totalMobiliario+= reservaMobiliario.getCantidad()* reservaMobiliario.getPresentation().getPrecio();
        }

        return (totalMobiliario*diasPrestamo);
    }


    /**
     * Elimina un registro de la base de datos
     * @param idCobro Llave primaria utilizada para la eilminacion del registro
     * @param request Información de la peticion
     */
    @Transactional
    public void delete(Long idCobro, HttpServletRequest request) {
        userValid(request);

        CobroEntity cobro = cobroRepository.findById(idCobro)
                .orElseThrow(()->new ValidationException("El id del cobro recibido no existe"));
        cobroRepository.deleteById(idCobro);
    }

    /**
     * Verifica que la peticion se haya hecho por un usuario de tipo ADMIN
     * @param request Informacion sobre la peticion
     */
    public void userValid(HttpServletRequest request){
        UserEntity actualUser = usuarioService.getActualUser(request);
        if(!actualUser.getRol().getRol().equals(ERole.ADMIN)) throw new ValidationException("Operacion restringida");
    }

    /**
     * Realiza una transaccion en sistema para actualizar el parametro valido de CobroEntity a 0
     * @param idCobro Llave primaria del cobro que sera actualizado
     * @param request Información de la peticion
     */
    @Transactional
    public void cancel(Long idCobro, HttpServletRequest request) {
        userValid(request);

        CobroEntity cobro = cobroRepository.findById(idCobro)
                .orElseThrow(()->new ValidationException("El id recibido no existe"));

        cobro.getReservacion().setStatus(3);

        if(cobro.getValido()){
            cobro.setValido(false);
        }else{
            throw new ValidationException("El cobro con id " + cobro.getIdCobro() + " ya se encuentra cancelado");
        }
        cobroRepository.save(cobro);
    }

    public Page<CobroEntity> valid(Pageable pageable) {
        Page<CobroEntity> validCobrosPage = cobroRepository.findByValid(pageable);
        validCobrosPage.forEach(System.out::println);
        return validCobrosPage;
    }

    public Page<CobroEntity> inValid(Pageable pageable) {
        Page<CobroEntity> inValidCobrosPage = cobroRepository.findByCanceled(pageable);
        inValidCobrosPage.forEach(System.out::println);
        return inValidCobrosPage;
    }

    public Page<CobroEntity> getAll(Pageable pageable) {
        return cobroRepository.findAll(pageable);

    }

    // Solo un ADMIN puede actualizar la informacion del cobro
    public UpdateCobroDTO update(UpdateCobroDTO updateCobroDTO, HttpServletRequest request) {
        userValid(request);

        CobroEntity cobro = cobroRepository.findById(updateCobroDTO.idCobro())
                .orElseThrow(()->new ValidationException("El id del cobro: "+ updateCobroDTO.idCobro() +" no existe"));

        if(cobro.getValido()){
            if(updateCobroDTO.total()>=0){
                cobro.setTotal(updateCobroDTO.total());
            }
        }else{
            throw new ValidationException("El cobro ha sido cancelado, no es posible editarlo. " +
                    "Solo se puede eliminar");
        }
        return new UpdateCobroDTO(cobroRepository.save(cobro));
    }

    @Transactional
    public double totalPerodo(TotalPeriodoDTO totalPeriodoDTO) {
        if(totalPeriodoDTO.fechaInicio().isAfter(LocalDateTime.now()) ||
                totalPeriodoDTO.fechaFinal().isAfter(LocalDateTime.now())){
            throw new ValidationException("La fecha inicial/final no puede ser superior a la fecha actual");
        }
        if(totalPeriodoDTO.fechaInicio().isEqual(totalPeriodoDTO.fechaFinal())){
            throw new ValidationException("La fecha inicial y la fecha final no pueden ser iguales");
        }
        if(totalPeriodoDTO.fechaFinal().isBefore(totalPeriodoDTO.fechaInicio())){
            throw new ValidationException("La fecha final no puede ser inferior a la fecha inicial");
        }

        List<ReservacionEntity> listaPeriodo = reservacionRepository
                .findAllByPeriodo(totalPeriodoDTO.fechaInicio(), totalPeriodoDTO.fechaFinal());
        Set<LocalDateTime> fechas = listaPeriodo.stream().map(ReservacionEntity::getFechaRecepcion).collect(Collectors.toSet());
        double total = 0;

        total = fechas.stream().mapToDouble(this::getTotalFecha).sum();

        /*System.out.println("El periodo evaluado es del " + totalPeriodoDTO.fechaInicio()+" al " +
                totalPeriodoDTO.fechaFinal() + " Total: "+ total);*/
        return total;
    }

    public List<CobroEntity> findByFecha(LocalDateTime fecha) {
        return cobroRepository.findAllByFecha(fecha);

    }
}
