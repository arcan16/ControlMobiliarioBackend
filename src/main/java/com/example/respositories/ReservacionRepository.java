package com.example.respositories;

import com.example.dto.reservacion.MobiliarioPrestadoDTO;
import com.example.models.ReservacionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservacionRepository extends JpaRepository<ReservacionEntity,Long> {

    @Query("""
        SELECT NEW com.example.dto.reservacion.MobiliarioPrestadoDTO(
            pm.mobiliario.idMobiliario,
            m.descripcion,
            SUM(rm.cantidad * pm.cantidad) as total
        )
        FROM ReservacionEntity r
        JOIN ReservaMobiliarioEntity rm ON r.idReservacion = rm.reservacion.idReservacion
        JOIN PresentationMobiliarioEntity pm ON pm.presentation.idPresentacion = rm.presentation.idPresentacion
        JOIN PresentationEntity p ON pm.presentation.idPresentacion = p.idPresentacion
        JOIN MobiliarioEntity m ON pm.mobiliario.idMobiliario = m.idMobiliario
        where r.fechaEntrega <= :fechaEntrega and r.fechaRecepcion >= :fechaEntrega
        GROUP BY m.descripcion
    """)
    List<MobiliarioPrestadoDTO> prestamosVigentes(@Param("fechaEntrega") LocalDateTime fechaEntrega);
//@Query(value = "SELECT NEW com.example.dto.reservacion.MobiliarioPrestadoDTO(" +
//        "pm.mobiliario.idMobiliario, " +
//        "m.descripcion, " +
//        "SUM(rm.cantidad * pm.cantidad) as total" +
//        ") " +
//        "FROM ReservacionEntity r " +
//        "JOIN ReservaMobiliarioEntity rm ON r.idReservacion = rm.reservacion.idReservacion " +
//        "JOIN PresentationMobiliarioEntity pm ON pm.presentation.idPresentacion = rm.presentation.idPresentacion " +
//        "JOIN PresentationEntity p ON pm.presentation.idPresentacion = p.idPresentacion " +
//        "JOIN MobiliarioEntity m ON pm.mobiliario.idMobiliario = m.idMobiliario " +
//        "where r.fechaEntrega <= :fechaEntrega and r.fechaRecepcion >= :fechaEntrega " +
//        "GROUP BY m.descripcion", nativeQuery = true)
//List<MobiliarioPrestadoDTO> prestamosVigentes(@Param("fechaEntrega") LocalDateTime fechaEntrega);
    @Query("""
            SELECT r FROM ReservacionEntity r WHERE r.fechaRecepcion = :fecha
            """)
    List<ReservacionEntity> findAllByFechaRecepcion(LocalDateTime fecha);

    @Query("""
            SELECT r FROM ReservacionEntity r WHERE r.fechaRecepcion >= :fechaInicial
            AND r.fechaRecepcion <= :fechaFinal
            """)
    List<ReservacionEntity> findAllByPeriodo(LocalDateTime fechaInicial, LocalDateTime fechaFinal);


    /**
     * Consulta las reservaciones que han sido entregadas (por lo que tendran un valor de 1 en el campo status)
     * @param pageable
     * @return
     */
    @Query("""
            SELECT r FROM ReservacionEntity r WHERE r.status = 1
            """)
    Page<ReservacionEntity> findAllByStatusActive(Pageable pageable);

    @Query("""
            SELECT r FROM ReservacionEntity r WHERE r.fechaEntrega >= CURDATE() AND r.status = 0
            """)
    Page<ReservacionEntity> prestamosVigentes2(Pageable pageable);

    @Query("""
            SELECT r FROM ReservacionEntity r WHERE r.fechaEntrega = CURDATE() and r.status = 0
            """)
    Page<ReservacionEntity> getEntregasDeldia(Pageable pageable);
    @Query("""
            SELECT r FROM ReservacionEntity r WHERE r.fechaRecepcion = CURDATE() AND r.status = 1
            OR (r.fechaRecepcion < CURDATE() AND r.status = 1)
            """)
    Page<ReservacionEntity> getRecepcionDeldia(Pageable pageable);

    @Query("""
            SELECT r FROM ReservacionEntity r
                        WHERE MONTH(r.fechaEntrega) = (MONTH(:fecha1) + 1 ) OR MONTH(r.fechaRecepcion) = (MONTH(:fecha2) + 1) OR r.status = 1
            """)
    Page<ReservacionEntity> getPeriodo(Pageable pageable, Date fecha1, Date fecha2);

    @Query("""
            SELECT COUNT(r) FROM ReservacionEntity r INNER JOIN ClientsEntity c ON c.idCliente = r.cliente.idCliente WHERE r.status =1 and r.cliente.idCliente = :idCliente
            """)
    Integer findAllToDelete(Long idCliente);

    @Modifying
    @Query("""
            DELETE FROM ReservacionEntity r WHERE r.cliente.idCliente = :idCliente
            """)
    @Transactional
    int deleteAllByClient(Long idCliente);
}