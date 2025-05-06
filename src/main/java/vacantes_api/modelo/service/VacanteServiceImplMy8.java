package vacantes_api.modelo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import vacantes_api.modelo.entity.Vacante;
import vacantes_api.modelo.repository.ISolicitudRepository;
import vacantes_api.modelo.repository.IVacanteRepository;

/**
 * Implementación del servicio {@link IVacanteService} para la gestión
 * de vacantes. Incluye lógica personalizada para cancelación lógica
 * y eliminación de solicitudes asociadas.
 */
@Service
public class VacanteServiceImplMy8 extends GenericoCRUDServiceImplMy8<Vacante, Integer> implements IVacanteService {

    @Autowired
    private IVacanteRepository vacanteRepository;

    @Autowired
    private ISolicitudRepository solicitudRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    protected IVacanteRepository getRepository() {
        return vacanteRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vacante> findByNombre(String nombre) {
        return vacanteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vacante> findByCategoriaId(Integer idCategoria) {
        return vacanteRepository.findByCategoriaIdCategoria(idCategoria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vacante> findBySalario(Double salario) {
        return vacanteRepository.findBySalarioGreaterThanEqual(salario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vacante> findByCategoriaNombre(String nombre) {
        return vacanteRepository.findByCategoriaNombreContainingIgnoreCase(nombre);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vacante> findByEmpresaNombreEmpresa(String nombreEmpresa) {
        return vacanteRepository.findByEmpresaNombreEmpresaContainingIgnoreCase(nombreEmpresa);
    }

    /**
     * Sobrescribe el método delete para aplicar lógica de cancelación lógica
     * de la vacante en lugar de eliminarla físicamente, siempre que no tenga
     * solicitudes adjudicadas.
     *
     * @param id ID de la vacante.
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        Vacante v = vacanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacante no encontrada"));

        boolean soliAdjudicada = solicitudRepository.existsByVacanteIdVacanteAndEstado(id, 1);
        if (soliAdjudicada) {
            throw new IllegalStateException(
                    "No se puede cancelar esta vacante: existe al menos una solicitud adjudicada");
        }

        solicitudRepository.deleteByVacanteId(id);

        v.setEstatus(Vacante.Estatus.CANCELADA);
        vacanteRepository.save(v);
    }

    /**
     * Sobrescribe el método update para eliminar las solicitudes asociadas
     * si el estado de la vacante cambia a "CANCELADA".
     *
     * @param nueva vacante con los datos actualizados.
     * @return vacante guardada tras aplicar la actualización.
     */
    @Override
    @Transactional
    public Vacante update(Vacante nueva) {
        Vacante anterior = vacanteRepository.findById(nueva.getIdVacante())
                .orElseThrow(() -> new EntityNotFoundException("Vacante no encontrada"));

        boolean pasaACancelada = anterior.getEstatus() != Vacante.Estatus.CANCELADA
                && nueva.getEstatus() == Vacante.Estatus.CANCELADA;

        if (pasaACancelada) {
            solicitudRepository.deleteByVacanteId(nueva.getIdVacante());
        }

        return vacanteRepository.save(nueva);
    }

}
