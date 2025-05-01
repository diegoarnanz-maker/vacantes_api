package vacantes_api.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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


	@Override
	@Transactional
	public void deleteByVacanteId(int vacanteId) {
		
		solicitudRepository.deleteByVacanteId(vacanteId);
		
	}


	@Override
	@Transactional
	public void adjudicarSolicitud(int idSolicitud) {
		//Se carga la solicitud seleccionada
		
		Solicitud solSelect= solicitudRepository.findById(idSolicitud)
				.orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada"));
		
		
		//Se cambia el estado a adjudicada (1) y se guarda
		
		solSelect.setEstado(1);
		solicitudRepository.save(solSelect);
		
		// Se rechaza al resto de solicitudes, cambiándoles el estado a = 2 (indicado en el repository)
		
		int vacanteId= solSelect.getVacante().getIdVacante();
		
		solicitudRepository.rejectOthers(vacanteId, idSolicitud);
		
		
	}


	@Override
	public void rechazarSolicitud(int idSolicitud) {
		//Se carga la solicitud que se quiere rechazar
		
		Solicitud solicitud= solicitudRepository.findById(idSolicitud)
				.orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada"));
		
		int vacanteId = solicitud.getVacante().getIdVacante();
		
		//Si esta solicitud ya estaba adjudicada (estado= 1)
		if(solicitud.getEstado()==1) {
			solicitudRepository.resetAllEstado(vacanteId); //ponemos todas las solicitudes de esa vacante a pendiente
		}
		
		//Y finalmente rechazamos la solicitud seleccionada y guardamos los cambios:
		solicitud.setEstado(2);
		solicitudRepository.save(solicitud);
		
	}

}
