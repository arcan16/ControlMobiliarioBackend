create table tipo_mobiliario(
    id_tipo bigint NOT NULL AUTO_INCREMENT,
    nombre varchar(50) not null unique,
    fecha_creacion date not null,
    id_usuario bigint not null,

    PRIMARY KEY(id_tipo),
    CONSTRAINT fk_id_tipo FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)
)ENGINE=InnoDB;

create table mobiliario(
    id_mobiliario bigint not null auto_increment,
    id_tipo bigint not null,
    descripcion varchar(150) not null unique,
    cantidad int not null,
    fecha_creacion date not null,
    ultima_actualizacion date not null,
    id_usuario bigint not null,

    PRIMARY KEY(id_mobiliario),
    CONSTRAINT fk_id_usuario_mobiliario_ FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_id_tipo_ FOREIGN KEY(id_tipo) REFERENCES tipo_mobiliario(id_tipo)

)ENGINE=InnoDB;

create table presentacion(
    id_presentacion bigint not null auto_increment,
    descripcion varchar(50) not null,
    precio float,
    id_usuario bigint not null,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id_presentacion),
    CONSTRAINT fk_ud_usuario_presentacion_ FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)
)ENGINE=InnoDB;


create table presentacion_mobiliario(
    id_presentacion bigint not null,
    id_mobiliario bigint not null,
    cantidad int,

    PRIMARY KEY(id_presentacion, id_mobiliario),
    CONSTRAINT fk_presentacion_id FOREIGN KEY(id_presentacion) REFERENCES presentacion(id_presentacion) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_mobiliario_id FOREIGN KEY(id_mobiliario) REFERENCES mobiliario(id_mobiliario) ON DELETE NO ACTION ON UPDATE CASCADE
)ENGINE=InnoDB;