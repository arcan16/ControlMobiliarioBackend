package com.example.service;

import com.example.dto.presentation.*;
import com.example.dto.presentation.validations.ValidateStock;
import com.example.models.*;
import com.example.respositories.MobiliarioRepository;
import com.example.respositories.PresentationMobiliarioRepository;
import com.example.respositories.PresentationRepository;
import com.example.service.users.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PresentationService {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private PresentationMobiliarioRepository presentationMobiliarioRepository;
    @Autowired
    private MobiliarioRepository mobiliarioRepository;
    @Autowired
    private ValidateStock validateStock;

    public void create(CreatePresentationDTO presentationDTO, HttpServletRequest request) {
        // Obtenemos el usuario autor
        UserEntity actualUser = usuarioService.getActualUser(request);
        if(presentationRepository.existsByDescripcion(presentationDTO.description())){
            throw new ValidationException("La descripcion ya existe, no puede haber duplicados");
        }
        // Creamos una el objeto de la presentacion que sera almacenada y lo guardamos
        PresentationEntity presentationEntity = new PresentationEntity(presentationDTO,actualUser);
        presentationEntity.setUsuario(actualUser);
        presentationEntity = presentationRepository.save(presentationEntity);


        /*
         * El siguiente codigo realiza una iteracion sobre la lista de elementos y crea una conexion
         * a la base de datos en cada uno de ellos para realizar la transaccion
         */
/*
        PresentationEntity presentacionGuardada = presentationEntity;

        presentationDTO.presentationMobiliarioList().forEach(data->{
            PresentationMobiliarioEntityId id = new PresentationMobiliarioEntityId();
            MobiliarioEntity mobi = mobiliarioRepository.findById(data.idMobiliario())
                    .orElseThrow(()->new ValidationException("id de mobiliario no encontrado"));
            id.setMobiliario(data.idMobiliario());
            id.setPresentation(presentacionGuardada.getIdPresentacion());
            PresentationMobiliarioEntity presentationMobiliario =
                    new PresentationMobiliarioEntity(id,mobi,presentacionGuardada,data.cantidad());
            presentationMobiliarioRepository.save(presentationMobiliario);
            System.out.println(presentationMobiliario);
        });*/

        /*
        * El siguiente codigo almacena una lista de informacion utilizando una sola conexion a la base de datos
        * */
        PresentationEntity presentacionGuardada = presentationEntity;

        List<PresentationMobiliarioEntity> listaPresMobiliario =
                presentationDTO.presentationMobiliarioList().stream().map(mobiliario ->{
                    PresentationMobiliarioEntityId id = new PresentationMobiliarioEntityId();
                    MobiliarioEntity mobi = mobiliarioRepository.findById(mobiliario.idMobiliario())
                            .orElseThrow(()->new ValidationException("id de mobiliario no encontrado"));
                    id.setIdMobiliario(mobiliario.idMobiliario());
                    id.setIdPresentacion(presentacionGuardada.getIdPresentacion());
                    // Valildamos que la cantidad de un mobiliario utilizado este dentro de las capacidades
                    validateStock.validate(mobi,mobiliario);
                    if(mobiliario.cantidad()>mobi.getCantidad()){
                        throw new ValidationException("Error. La presentacion"+ mobi.getDescripcion()
                                +" tiene una existencia de "+ mobiliario.cantidad() +
                                " y estas intentando usar " + mobi.getCantidad() + " unidades");
                    }
                    return new PresentationMobiliarioEntity(id,presentacionGuardada,
                            mobi,
                            mobiliario.cantidad());
                }).toList();

        presentacionGuardada.setPresentationMobiliarioEntityList(listaPresMobiliario);
    }

    public PresentacionDTO completeUpdate(UpdatePresentationDTO updatePresentation, HttpServletRequest request) {
        UserEntity userActual = usuarioService.getActualUser(request);

        // Valida que exista la presentacion que se quiere actualizar
        PresentationEntity presentationToUpdate = presentationRepository.findById(updatePresentation.id())
                .orElseThrow(()->new ValidationException("No se encontro ningun registro con el id proporcionado"));

        // Actualiza la descripcion de la presentacion
        if(updatePresentation.descripcion()!=null && !updatePresentation.descripcion().isBlank()){
            if(!updatePresentation.descripcion().equals(presentationToUpdate.getDescripcion())){
                if(presentationRepository.existsByDescripcion(updatePresentation.descripcion())){
                    throw new ValidationException("La descripcion ya existe");
                }else{
                    presentationToUpdate.setDescripcion(updatePresentation.descripcion());
                }
            }
        }

        // Actualiza el precio de la presentacion
        if(updatePresentation.precio()!=null && !updatePresentation.precio().isNaN()){
            presentationToUpdate.setPrecio(updatePresentation.precio());
        }

        // Actualiza la lista de mobiliario dentro de la presentacion
        if(!updatePresentation.updatePresentationMobiliarioDTOList().isEmpty()){
            // Eliminamos la informacion de la lista anterior
            try {
                int eliminaciones = presentationMobiliarioRepository.deleteAllByPresentation(updatePresentation.id());
            }catch (Error e){
                System.out.println(e);
            }

            PresentationEntity presentacionGuardada = presentationToUpdate;

            List<PresentationMobiliarioEntity> listaPresMobiliario =
                    updatePresentation.updatePresentationMobiliarioDTOList().stream().map(mobiliario ->{

                        PresentationMobiliarioEntityId id = new PresentationMobiliarioEntityId();
                        System.out.println(mobiliario.idMobiliario());
                        MobiliarioEntity mobi = mobiliarioRepository.findById(mobiliario.idMobiliario())
                                .orElseThrow(()->new ValidationException("id de mobiliario no encontrado"));

                        id.setIdMobiliario(mobiliario.idMobiliario());

                        id.setIdPresentacion(presentacionGuardada.getIdPresentacion());
                        // Valildamos que la cantidad de un mobiliario utilizado este dentro de las capacidades
                        validateStock.validate(mobi,mobiliario);
                        if(mobiliario.cantidad()>mobi.getCantidad()){
                            throw new ValidationException("Error. La presentacion"+ mobi.getDescripcion()
                                    +" tiene una existencia de "+ mobiliario.cantidad() +
                                    " y estas intentando usar " + mobi.getCantidad() + " unidades");
                        }

                        return new PresentationMobiliarioEntity(id,presentacionGuardada,
                                mobi,
                                mobiliario.cantidad());
                    }).toList();

            System.out.println(listaPresMobiliario);
            listaPresMobiliario.forEach((el)->{
                presentacionGuardada.getPresentationMobiliarioEntityList().add(el);
            });
            System.out.println(presentacionGuardada);
            presentationRepository.save(presentacionGuardada);
        }
        return new PresentacionDTO(presentationToUpdate);
    }

    public PresentacionDTO update(UpdatePresentationDTO updatePresentation, HttpServletRequest request){
        UserEntity userActual = usuarioService.getActualUser(request);

        PresentationEntity presentationToUpdate = presentationRepository.findById(updatePresentation.id())
                .orElseThrow(()->new ValidationException("No se encontro ningun registro con el id proporcionado"));

        if(updatePresentation.descripcion()!=null && !updatePresentation.descripcion().isBlank()){
            if(!updatePresentation.descripcion().equals(presentationToUpdate.getDescripcion())){
               if(presentationRepository.existsByDescripcion(updatePresentation.descripcion())){
                   throw new ValidationException("La descripcion ya existe");
               }else{
                   presentationToUpdate.setDescripcion(updatePresentation.descripcion());
               }
            }
        }

        if(updatePresentation.precio()!=null && !updatePresentation.precio().isNaN()){
            presentationToUpdate.setPrecio(updatePresentation.precio());
        }

        if(!updatePresentation.updatePresentationMobiliarioDTOList().isEmpty()){
            // Actualizamos los registros que ya se encuentran enlazados
            for(CreatePresentationMobiliarioDTO presentationMobiliarioDTO : updatePresentation.updatePresentationMobiliarioDTOList()){
                int existeRegistro = 0;
                for (PresentationMobiliarioEntity presentationMobiliario:presentationToUpdate.getPresentationMobiliarioEntityList()){
                    MobiliarioEntity mobi = mobiliarioRepository.findById(presentationMobiliario.getMobiliario().getIdMobiliario())
                            .orElseThrow(()->new ValidationException("No existe el id: " + presentationMobiliario.getMobiliario().getIdMobiliario()));
                    validateStock.validate(mobi, presentationMobiliario.getMobiliario());
                    if(presentationMobiliario.getMobiliario().getIdMobiliario().equals(presentationMobiliarioDTO.idMobiliario())){

                        presentationMobiliario.setCantidad(presentationMobiliarioDTO.cantidad());
                        existeRegistro=1;
                        break;
                    }
                }
                // Agregar los elementos que no estaban en nuestro registro
                if(existeRegistro==0){
                    PresentationMobiliarioEntityId id = new PresentationMobiliarioEntityId();
                    id.setIdPresentacion(presentationToUpdate.getIdPresentacion());
                    id.setIdMobiliario(presentationMobiliarioDTO.idMobiliario());
                    PresentationMobiliarioEntity pm = new PresentationMobiliarioEntity();

                    MobiliarioEntity me= mobiliarioRepository.findById(presentationMobiliarioDTO.idMobiliario())
                            .orElseThrow(()->new ValidationException("El id del mobiliario proporcionado no existe"));

                    pm.setId(id);
                    pm.setPresentation(presentationToUpdate);
                    pm.setMobiliario(me);
                    pm.setCantidad(presentationMobiliarioDTO.cantidad());
                    presentationToUpdate.getPresentationMobiliarioEntityList().add(pm);
                }
            }

            // Guardamos las modificaciones
            presentationToUpdate.setUsuario(userActual);
            presentationToUpdate.setUltimaActualizacion(LocalDateTime.now());
            presentationRepository.save(presentationToUpdate);
        }
        return new PresentacionDTO(presentationToUpdate);
    }

    public void deletePresentationMobiliario(DeletePresentationMobiliario deletePresentationMobiliario) {
        PresentationEntity presentation = presentationRepository.findById(deletePresentationMobiliario.idPresentation())
                .orElseThrow(()->new ValidationException("El id de la presentacion proporcionado no existe"));
        /*for(PresentationMobiliarioEntity presentationMobiliario : presentation.getPresentationMobiliarioEntityList()){
            if(presentationMobiliario.getMobiliario().getIdMobiliario().equals(deletePresentationMobiliario.idMobiliario())){
                System.out.println("Vamos a eliminar " + presentationMobiliario.getMobiliario().getIdMobiliario());
                System.out.println("Parametro recibido "+ deletePresentationMobiliario.idMobiliario());
                presentation.getPresentationMobiliarioEntityList().remove(presentationMobiliario);
            }
        }*/
        Iterator<PresentationMobiliarioEntity> iterator = presentation.getPresentationMobiliarioEntityList().iterator();
        while (iterator.hasNext()) {
            PresentationMobiliarioEntity presentationMobiliario = iterator.next();
            if (presentationMobiliario.getMobiliario().getIdMobiliario().equals(deletePresentationMobiliario.idMobiliario())) {
                System.out.println("Vamos a eliminar " + presentationMobiliario.getMobiliario().getIdMobiliario());
                System.out.println("Parametro recibido " + deletePresentationMobiliario.idMobiliario());
                iterator.remove(); // Utiliza el método remove() del iterador para eliminar el elemento

                presentationMobiliarioRepository.delete(presentationMobiliario);
            }
        }
        presentationRepository.save(presentation);
        System.out.println(new PresentacionDTO(presentation));
    }


}
