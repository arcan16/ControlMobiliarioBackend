package com.example.service;

import com.example.dto.clients.ClienteDTO;
import com.example.dto.clients.CreateClientDTO;
import com.example.dto.clients.DetalleCompletoClienteDTO;
import com.example.dto.clients.UpdateClientDTO;
import com.example.dto.empleados.validations.EmployeNameOnlyCharacters;
import com.example.dto.empleados.validations.FormatPhone;
import com.example.infra.security.jwt.JwtUtils;
import com.example.models.ClientsEntity;
import com.example.models.UserEntity;
import com.example.respositories.ClientsRepository;
import com.example.respositories.ReservacionRepository;
import com.example.respositories.UserRepository;
import com.example.service.users.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientsService {

    @Autowired
    private FormatPhone formatPhone;

    @Autowired
    private EmployeNameOnlyCharacters onlyCharacters;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservacionRepository reservacionRepository;

    /**
     * Recibe la informacion que sera utilizada para validarla y crear un nuevo registro
     * @param dataClient
     * @param request peticion de la que obtendremos el token para obtener el usuario
     * @return Regresa el registro que fue creado correctamente
     */
    public ClientsEntity createClient(@Valid CreateClientDTO dataClient, HttpServletRequest request){
        onlyCharacters.validate(dataClient.nombre());
        formatPhone.validate(dataClient.telefono());

        if(clientsRepository.existsByNombre(dataClient.nombre()))
            throw new ValidationException("El cliente ya existe");

        UserEntity userEntity = usuarioService.getActualUser(request);

        ClientsEntity client = new ClientsEntity(dataClient,userEntity);

        return clientsRepository.save(client);
    }

    public void exists(Long id) {
        if(!clientsRepository.existsById(id)){
            throw new ValidationException("El cliente con id: "+id+" no existe, favor de verificar");
        }
    }

/*    public UserEntity getActualUser(HttpServletRequest request){
        String token = jwtUtils.getTokenFromHeader(request);
        String username = jwtUtils.getUsernameFromToken(token);
        return userRepository.getReferenceByUsername(username);
    }*/

    public DetalleCompletoClienteDTO update(UpdateClientDTO clientDTO, HttpServletRequest request) {
        exists(clientDTO.id());
        ClientsEntity client = clientsRepository.getReferenceById(clientDTO.id());
        UserEntity actualUser = usuarioService.getActualUser(request);
        if(clientDTO.nombre()!=null && !clientDTO.nombre().isBlank()){
            client.setNombre(clientDTO.nombre());
        }
        if(clientDTO.direccion()!=null && !clientDTO.direccion().isBlank()){
            client.setDireccion(clientDTO.direccion());
        }
        if(clientDTO.telefono()!=null && !clientDTO.telefono().isBlank()){
            client.setTelefono(clientDTO.telefono());
        }
        client.setUsuario(actualUser);

        return new DetalleCompletoClienteDTO(clientsRepository.save(client));
    }

    public ClientsEntity getClientByName(ClienteDTO cliente) {
        return clientsRepository.getClientByName(cliente.cliente())
                .orElseThrow(()-> new ValidationException("El cliente no existe"));
    }

    public ObjectNode deleteAll(Long id) {
        ClientsEntity client = clientsRepository.getReferenceById(id);
        Integer reservacionesPrestadas = reservacionRepository.
                findAllToDelete(client.getIdCliente());
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        System.out.println(reservacionesPrestadas);
        if(reservacionesPrestadas>0){
            //Retornar un json "No se pueden eliminar los registros"

            jsonResponse.put("message","No es posible eliminar debido a que tiene prestamos activos ");
            jsonResponse.put("prestamo","vigente");
            return jsonResponse;
        }else{
            jsonResponse.put("prestamo","finalizado");
            Integer deletes = reservacionRepository.deleteAllByClient(client.getIdCliente());
            clientsRepository.deleteById(client.getIdCliente());
            return jsonResponse;
        }
    }
}
