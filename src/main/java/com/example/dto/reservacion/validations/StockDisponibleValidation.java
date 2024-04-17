package com.example.dto.reservacion.validations;

import com.example.dto.reservacion.ReservaPrestamoDTO;
import com.example.models.MobiliarioEntity;
import com.example.models.ReservacionEntity;
import com.example.respositories.MobiliarioRepository;
import com.example.respositories.ReservacionRepository;
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
