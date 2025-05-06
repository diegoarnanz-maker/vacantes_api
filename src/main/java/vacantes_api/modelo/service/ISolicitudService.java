package vacantes_api.modelo.service;

import java.util.List;
import java.util.Optional;

import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;

/**
 * Interfaz de servicio para la gestión de solicitudes de empleo.
 * Extiende las operaciones CRUD genéricas definidas en {@link IGenericoCRUD}.
 */
public interface ISolicitudService extends IGenericoCRUD<Solicitud, Integer> {

    /**
     * Obtiene todas las solicitudes realizadas por un usuario a partir de su email.
     *
     * @param email email del usuario.
     * @return lista de solicitudes asociadas al usuario.
     */
    List<Solicitud> findByUsuarioEmail(String email);

    /**
     * Busca una solicitud específica realizada por un usuario para una vacante
     * concreta.
     *
     * @param vacante vacante objetivo de la solicitud.
     * @param usuario usuario que realizó la solicitud.
     * @return solicitud encontrada, si existe.
     */
    Optional<Solicitud> findByVacanteAndUsuario(Vacante vacante, Usuario usuario);

    /**
     * Obtiene todas las solicitudes asociadas a una vacante.
     *
     * @param vacante vacante a consultar.
     * @return lista de solicitudes correspondientes.
     */
    List<Solicitud> findByVacante(Vacante vacante);

    /**
     * Elimina todas las solicitudes vinculadas a una vacante.
     *
     * @param vacanteId identificador de la vacante.
     */
    void deleteByVacanteId(int vacanteId);

    /**
     * Marca una solicitud como adjudicada y actualiza el estado de las demás
     * solicitudes asociadas a la vacante.
     *
     * @param idSolicitud ID de la solicitud que se adjudica.
     */
    void adjudicarSolicitud(int idSolicitud);

    /**
     * Marca una solicitud adjudicada como rechazada (permite deshacer
     * adjudicaciones).
     *
     * @param idSolicitud ID de la solicitud a rechazar.
     */
    void rechazarSolicitud(int idSolicitud);
}
