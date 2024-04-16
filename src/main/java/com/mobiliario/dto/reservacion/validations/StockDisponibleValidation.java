package com.mobiliario.dto.reservacion.validations;

import com.mobiliario.dto.reservacion.ReservaPrestamoDTO;
import com.mobiliario.models.MobiliarioEntity;
import com.mobiliario.models.ReservacionEntity;
import com.mobiliario.respositories.MobiliarioRepository;
import com.mobiliario.respositories.ReservacionRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockDisponibleValidation implements ReservaValidation{

    @Autowired
    private ReservacionRepository reservacionRepository;

    @Autowired
    private MobiliarioRepository mobiliarioRepository;



    @Override
    public void Validate(ReservaPrestamoDTO reservaMobiliario, ReservacionEntity reservacion) {
        reservacionRepository.prestamosVigentes(reservacion.getFechaEntrega()).forEach(mobiliarioPrestado->{
            /*System.out.println("Mobiliario prestado " + mobiliarioPrestado.total() +" "+
                    mobiliarioPrestado.descripcion());*/
            MobiliarioEntity mobiliario = mobiliarioRepository.findById(mobiliarioPrestado.id())
                    .orElseThrow(()->new ValidationException("El id del mobiliario " + mobiliarioPrestado.id()
                            + " no existe"));
//            System.out.println("Existencia de " + mobiliario.getDescripcion() + " " + mobiliario.getCantidad());
            if(mobiliarioPrestado.total()>= mobiliario.getCantidad()){
                throw new ValidationException("El mobiliario con id " + mobiliario.getIdMobiliario() +
                        " no esta disponible para la fecha seleccionada");
            }
        });

    }
    public void Validate(ReservacionEntity reservacion) {
        reservacionRepository.prestamosVigentes(reservacion.getFechaEntrega()).forEach(mobiliarioPrestado->{
            /*System.out.println("Mobiliario prestado " + mobiliarioPrestado.total() +" "+
                    mobiliarioPrestado.descripcion());*/
            MobiliarioEntity mobiliario = mobiliarioRepository.findById(mobiliarioPrestado.id())
                    .orElseThrow(()->new ValidationException("El id del mobiliario " + mobiliarioPrestado.id()
                            + " no existe"));
            /*System.out.println("Existencia de " + mobiliario.getDescripcion() + " " + mobiliario.getCantidad());*/
            if(mobiliarioPrestado.total()> mobiliario.getCantidad()){
                throw new ValidationException("El mobiliario con id " + mobiliario.getIdMobiliario() +
                        " no esta disponible para la fecha seleccionada");
            }
        });

    }
}
