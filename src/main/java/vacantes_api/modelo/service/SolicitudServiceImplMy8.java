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

/**
 * Implementación del servicio {@link ISolicitudService} que gestiona las
 * operaciones
 * de negocio relacionadas con las solicitudes de empleo.
 */
@Service
public class SolicitudServiceImplMy8 extends GenericoCRUDServiceImplMy8<Solicitud, Integer>
		implements ISolicitudService {

	@Autowired
	private ISolicitudRepository solicitudRepository;

	@Override
	protected ISolicitudRepository getRepository() {
		return solicitudRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Solicitud> findByUsuarioEmail(String email) {
		return solicitudRepository.findByUsuarioEmail(email);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Solicitud> findByVacanteAndUsuario(Vacante vacante, Usuario usuario) {
		return solicitudRepository.findByVacanteAndUsuario(vacante, usuario);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Solicitud> findByVacante(Vacante vacante) {
		return solicitudRepository.findByVacante(vacante);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteByVacanteId(int vacanteId) {
		solicitudRepository.deleteByVacanteId(vacanteId);
	}

	/**
	 * Adjudica una solicitud (estado = 1) y marca las demás solicitudes de la
	 * vacante como rechazadas (estado = 2).
	 *
	 * @param idSolicitud ID de la solicitud adjudicada.
	 */
	@Override
	@Transactional
	public void adjudicarSolicitud(int idSolicitud) {
		Solicitud solSelect = solicitudRepository.findById(idSolicitud)
				.orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada"));

		solSelect.setEstado(1);
		solicitudRepository.save(solSelect);

		int vacanteId = solSelect.getVacante().getIdVacante();
		solicitudRepository.rejectOthers(vacanteId, idSolicitud);
	}

	/**
	 * Rechaza una solicitud y, si estaba adjudicada, restablece todas las
	 * solicitudes de la vacante a estado pendiente (estado = 0).
	 *
	 * @param idSolicitud ID de la solicitud a rechazar.
	 */
	@Override
	public void rechazarSolicitud(int idSolicitud) {
		Solicitud solicitud = solicitudRepository.findById(idSolicitud)
				.orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada"));

		int vacanteId = solicitud.getVacante().getIdVacante();

		if (solicitud.getEstado() == 1) {
			solicitudRepository.resetAllEstado(vacanteId);
		}

		solicitud.setEstado(2);
		solicitudRepository.save(solicitud);
	}
}
