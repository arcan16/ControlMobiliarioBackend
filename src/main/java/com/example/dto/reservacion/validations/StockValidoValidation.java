package com.example.dto.reservacion.validations;

import com.example.dto.reservacion.ReservaPrestamoDTO;
import com.example.models.PresentationEntity;
import com.example.models.ReservacionEntity;
import com.example.respositories.PresentationRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockValidoValidation implements ReservaValidation{

    @Autowired
    private PresentationRepository presentationRepository;

    @Override
    public void Validate(ReservaPrestamoDTO reservaMobiliario, ReservacionEntity reservacion) {
        PresentationEntity presentation = presentationRepository.findById(reservaMobiliario.idPresentacion())
                .orElseThrow(()->new ValidationException("El id " + reservaMobiliario.idPresentacion() + " " +
                        "no existe"));
//1 Codigo abajo

        presentation.getPresentationMobiliarioEntityList().forEach(el-> {
            int totalSolicitado = el.getCantidad()* reservaMobiliario.cantidad();
            int stock = el.getMobiliario().getCantidad();
//2 Codigo abajo
            if(totalSolicitado>stock){
                throw new ValidationException("Inventario insuficiente. " +
                        "No contamos con suficientes "+el.getMobiliario().getDescripcion()+
                        "para cubrir su peticion");
            }
        });
    }
}
/*
1
    System.out.println("La presentacion buscada sera " + presentation);
2
    System.out.println("Mobiliario: "
                    + el.getMobiliario().getDescripcion() + " stock: " +el.getMobiliario().getCantidad());
            System.out.println("Total solicitado "+(el.getCantidad()* reservaMobiliario.cantidad()));
*/
