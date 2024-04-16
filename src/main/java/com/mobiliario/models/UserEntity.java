package com.mobiliario.models;

import com.mobiliario.dto.users.RegistroUsuariosDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "UserEntity")
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {
  /*  @Autowired
    private PasswordEncoder passwordEncoder;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String username;
    private String password;
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    private RolesEntity rol;

    public UserEntity(RegistroUsuariosDTO usuario) {
        this.username=usuario.username();
        this.password= usuario.password();
        this.email=usuario.email();
    }

    public UserEntity(RegistroUsuariosDTO usuario, RolesEntity rolesEntity) {
        this(usuario);
        this.rol=rolesEntity;
    }
    public UserEntity(String usuario,String password, RolesEntity rol){
        this.username = usuario;
        this.password = password;
        this.rol= rol;
        this.email="test@admin.com";
    }
}
