USE mobiliario_eventos;

# Eliminando V1 
drop table roles, usuarios, empleados;
delete from flyway_schema_history where version >0;

# Eliminando V2
drop table mobiliario, presentacion, presentacion_mobiliario, tipo_mobiliario;
delete from flyway_schema_history where version >1;

# Eliminando V3
drop table clientes, reservacion, reserva_mobiliario, cobro;
delete from flyway_schema_history where version =3;