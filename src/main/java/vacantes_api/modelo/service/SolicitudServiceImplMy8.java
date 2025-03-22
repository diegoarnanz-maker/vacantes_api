package vacantes_api.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.entity.Solicitud;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsuarioEmail'");
    }

}
