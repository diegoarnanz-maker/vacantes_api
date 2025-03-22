package vacantes_api.modelo.service;

import java.util.List;
import java.util.Optional;

import vacantes_api.modelo.entity.Solicitud;

public interface ISolicitudService extends IGenericoCRUD<Solicitud, Integer> {

    Optional<Solicitud> findByNombre(String nombre);
    List<Solicitud> findByUsuarioEmail(String email);

}
