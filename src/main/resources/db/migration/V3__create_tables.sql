create table clientes(
    id_cliente bigint not null auto_increment,
    nombre varchar(100) not null,
    direccion varchar(100) not null,
    telefono varchar(12) not null,
    id_usuario bigint not null,

    PRIMARY KEY(id_cliente),
    CONSTRAINT fk_id_usuario_clientes_ FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario) ON DELETE NO ACTION ON UPDATE CASCADE
);

create table reservacion(
    id_reservacion bigint not null auto_increment,
    id_cliente bigint not null,
    id_usuario bigint not null,
    fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    direccion_entrega varchar(200) not null,
    fecha_entrega date not null,
    fecha_recepcion date not null,
    status tinyint default 0,
    PRIMARY KEY(id_reservacion),

    CONSTRAINT fk_id_cliente_ FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_id_usuario_ FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
);

create table reserva_mobiliario(
    id_presentacion bigint not null,
    id_reservacion bigint not null,
    cantidad int not null,

    CONSTRAINT fk_presentacion_id_ FOREIGN KEY(id_presentacion) REFERENCES presentacion(id_presentacion) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_reservacion_prestamo_id FOREIGN KEY(id_reservacion) REFERENCES reservacion(id_reservacion) ON DELETE CASCADE ON UPDATE CASCADE
);

create table cobro(
    id_cobro bigint not null auto_increment,
    id_reservacion bigint not null,
    total float,
    fecha timestamp default current_timestamp,
    id_usuario bigint not null,
    valido BOOLEAN default 0,

    PRIMARY KEY(id_cobro),
    CONSTRAINT fk_id_reservacion FOREIGN KEY(id_reservacion) REFERENCES reservacion(id_reservacion) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_id_usuario FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario) ON DELETE NO ACTION ON UPDATE CASCADE
);