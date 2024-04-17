package com.example.service;

import com.example.dto.mobiliario.CreateMobiliarioDTO;
import com.example.dto.mobiliario.DetalleMobiliarioCompletoDTO;
import com.example.dto.mobiliario.UpdateMobiliarioDTO;
import com.example.models.MobiliarioEntity;
import com.example.models.TipoMobiliarioEntity;
import com.example.models.UserEntity;
import com.example.respositories.MobiliarioRepository;
import com.example.respositories.TipoMobiliarioRepository;
import com.example.service.users.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MobiliarioService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoMobiliarioRepository tipoMobiliarioRepository;

    @Autowired
    private MobiliarioRepository mobiliarioRepository;

    public DetalleMobiliarioCompletoDTO createMobiliario(CreateMobiliarioDTO mobiliarioDTO, HttpServletRequest request){
        UserEntity user = usuarioService.getActualUser(request);
        TipoMobiliarioEntity tipoMobiliario = tipoMobiliarioRepository.findById(mobiliarioDTO.tipo())
                .orElseThrow(()-> new ValidationException("El tipo de mobiliario indicado no existe"));

        MobiliarioEntity mobiliario = new MobiliarioEntity(tipoMobiliario,mobiliarioDTO,user);


        return new DetalleMobiliarioCompletoDTO(mobiliarioRepository.save(mobiliario));
    }

    public DetalleMobiliarioCompletoDTO update(UpdateMobiliarioDTO updateMobiliarioDTO, HttpServletRequest request) {
        UserEntity userEntity = usuarioService.getActualUser(request);
        MobiliarioEntity mobiliarioToUpdate = mobiliarioRepository.findById(updateMobiliarioDTO.id())
                .orElseThrow(()->new ValidationException("El id " + updateMobiliarioDTO.id() + " no existe"));
        TipoMobiliarioEntity tipoMobiliario;
        System.out.println(updateMobiliarioDTO.tipo());
        if(updateMobiliarioDTO.tipo()!=null && !updateMobiliarioDTO.tipo().isBlank()){
            tipoMobiliario=tipoMobiliarioRepository.findById(Long.parseLong(updateMobiliarioDTO.tipo()))
                    .orElseThrow(()->new ValidationException("El tipo de mobiliario indicado no fue encontrado"));
            mobiliarioToUpdate.setTipo(tipoMobiliario);
        }

        if(updateMobiliarioDTO.descripcion()!=null && !updateMobiliarioDTO.descripcion().isBlank()){
            if(!updateMobiliarioDTO.descripcion().equals(mobiliarioToUpdate.getDescripcion())){
                if(mobiliarioRepository.existsByDescripcion(updateMobiliarioDTO.descripcion())){
                    throw new ValidationException("La descripcion ya existe, no es posible duplicarlas");
                }
            }
            mobiliarioToUpdate.setDescripcion(updateMobiliarioDTO.descripcion());
        }
        if (updateMobiliarioDTO.cantidad()!=null){
            mobiliarioToUpdate.setCantidad(updateMobiliarioDTO.cantidad());
        }
        mobiliarioToUpdate.setUltimaActualizacion(LocalDateTime.now());
        mobiliarioToUpdate.setUsuario(userEntity);
        return new DetalleMobiliarioCompletoDTO(mobiliarioRepository.save(mobiliarioToUpdate));
    }
}