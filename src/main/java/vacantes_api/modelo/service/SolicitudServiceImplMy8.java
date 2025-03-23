package vacantes_api.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;
import vacantes_api.modelo.repository.ISolicitudRepository;

@Service
public class SolicitudServiceImplMy8 extends GenericoCRUDServiceImplMy8<Solicitud, Integer>
        implements ISolicitudService {

    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Override
    protected ISolicitudRepository getRepository() {
        return solicitudRepository;
    }

    @Override
    public Optional<Solicitud> findByNombre(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNombre'");
    }

    @Override
    public List<Solicitud> findByUsuarioEmail(String email) {
        return solicitudRepository.findByUsuarioEmail(email);

    }

    @Override
    public Optional<Solicitud> findByVacanteAndUsuario(Vacante vacante, Usuario usuario) {
        return solicitudRepository.findByVacanteAndUsuario(vacante, usuario);
    }

    @Override
    public List<Solicitud> findByVacante(Vacante vacante) {
        return solicitudRepository.findByVacante(vacante);
    }

}
