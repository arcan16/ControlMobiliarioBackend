create table roles(
    id_rol bigint not null auto_increment,
    rol varchar(10) not null unique,
    PRIMARY KEY(id_rol)
)ENGINE=InnoDB;

create table usuarios(
    id_usuario bigint not null auto_increment,
    username varchar(30) not null unique,
    password varchar(200) not null,
    email varchar(50) not null,
    id_rol bigint not null,
    PRIMARY KEY (id_usuario),
    CONSTRAINT pk_usuario_rol_ FOREIGN KEY(id_rol) REFERENCES roles(id_rol)
)ENGINE=InnoDB;


create table empleados(
    id_empleado bigint not null auto_increment,
    nombre varchar(50) not null,
    apellido varchar(50) not null,
    direccion varchar(100) not null,
    telefono varchar(12) not null,
    id_usuario bigint not null unique,

    PRIMARY KEY (id_empleado),
    CONSTRAINT fk_empleado_usuario FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)
)ENGINE=InnoDB;