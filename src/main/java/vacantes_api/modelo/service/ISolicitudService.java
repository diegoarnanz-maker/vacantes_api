package vacantes_api.modelo.service;

import java.util.List;
import java.util.Optional;

import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;

public interface ISolicitudService extends IGenericoCRUD<Solicitud, Integer> {


    List<Solicitud> findByUsuarioEmail(String email);

    Optional<Solicitud> findByVacanteAndUsuario(Vacante vacante, Usuario usuario);

    List<Solicitud> findByVacante(Vacante vacante);
    
    void deleteByVacanteId(int vacanteId);
    
    void adjudicarSolicitud(int idSolicitud);
    
    void rechazarSolicitud(int idSolicitud);
}
