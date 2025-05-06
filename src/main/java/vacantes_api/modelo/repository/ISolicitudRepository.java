package vacantes_api.modelo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;

/**
 * Repositorio JPA para la entidad {@link Solicitud}.
 * Gestiona operaciones de persistencia relacionadas con solicitudes de
 * vacantes.
 */
public interface ISolicitudRepository extends JpaRepository<Solicitud, Integer> {

    /**
     * Busca una solicitud concreta asociada a una vacante y un usuario.
     *
     * @param vacante vacante asociada.
     * @param usuario usuario que realiza la solicitud.
     * @return la solicitud si existe.
     */
    Optional<Solicitud> findByVacanteAndUsuario(Vacante vacante, Usuario usuario);

    /**
     * Devuelve todas las solicitudes realizadas por el usuario cuyo email coincida.
     *
     * @param email correo del usuario.
     * @return lista de solicitudes asociadas.
     */
    List<Solicitud> findByUsuarioEmail(String email);

    /**
     * Devuelve todas las solicitudes asociadas a una vacante.
     *
     * @param vacante vacante objetivo.
     * @return lista de solicitudes.
     */
    List<Solicitud> findByVacante(Vacante vacante);

    /**
     * Elimina todas las solicitudes asociadas a una vacante.
     *
     * @param vacanteId ID de la vacante.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Solicitud s WHERE s.vacante.idVacante = ?1")
    void deleteByVacanteId(Integer vacanteId);

    /**
     * Rechaza todas las solicitudes de una vacante excepto la seleccionada.
     *
     * @param vacanteId      ID de la vacante.
     * @param seleccionadaId ID de la solicitud adjudicada.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Solicitud s SET s.estado = 2 WHERE s.vacante.idVacante = :vacanteId AND s.idSolicitud <> :seleccionadaId")
    void rejectOthers(@Param("vacanteId") Integer vacanteId,
            @Param("seleccionadaId") Integer seleccionadaId);

    /**
     * Reinicia el estado de todas las solicitudes de una vacante a estado = 0
     * (pendiente).
     *
     * @param vacanteId ID de la vacante.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Solicitud s SET s.estado = 0 WHERE s.vacante.idVacante = ?1")
    void resetAllEstado(Integer vacanteId);

    /**
     * Verifica si existe al menos una solicitud con un estado específico para una
     * vacante.
     *
     * @param vacanteId ID de la vacante.
     * @param estado    estado a verificar (0, 1 o 2).
     * @return {@code true} si existe al menos una coincidencia.
     */
    boolean existsByVacanteIdVacanteAndEstado(Integer vacanteId, Integer estado);
}
