package vacantes_api.modelo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import vacantes_api.modelo.entity.Vacante;
import vacantes_api.modelo.repository.ISolicitudRepository;
import vacantes_api.modelo.repository.IVacanteRepository;

@Service
public class VacanteServiceImplMy8 extends GenericoCRUDServiceImplMy8<Vacante, Integer> implements IVacanteService {

    @Autowired
    private IVacanteRepository vacanteRepository;
    
    @Autowired
    private ISolicitudRepository solicitudRepository; 

    @Override
    protected IVacanteRepository getRepository() {
        return vacanteRepository;
    }

    @Override
    public List<Vacante> findByNombre(String nombre) {
        return vacanteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Vacante> findByCategoriaId(Integer idCategoria) {
        return vacanteRepository.findByCategoriaIdCategoria(idCategoria);
    }
    
    //Añadidos:

    @Override
    public List<Vacante> findBySalario(Double salario) {
        return vacanteRepository.findBySalarioGreaterThanEqual(salario);
    }
    

	@Override
	public List<Vacante> findByCategoriaNombre(String nombre) {
		return vacanteRepository.findByCategoriaNombreContainingIgnoreCase(nombre);
	}

	@Override
	public List<Vacante> findByEmpresaNombreEmpresa(String nombreEmpresa) {
		
		return vacanteRepository.findByEmpresaNombreEmpresaContainingIgnoreCase(nombreEmpresa);
	}
	
	//Se sobreescribe el método delete del CRUD Genérico para indicar que no queremos eliminar
	//la vacante si no cambiarle el estado a cancelada y eliminar todas las solicitudes asociadas a esta vacante
	
	@Override
    @Transactional
    public void delete(Integer id) {
		  // Comprobamos si existe la vacante
        Vacante v = vacanteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vacante no encontrada"));

        //Si tiene al menos una solicitud adjudicada no se deja cancelar:
        
        boolean soliAdjudicada = solicitudRepository.existsByVacanteIdVacanteAndEstado(id, 1);
        if(soliAdjudicada) {
        	throw new IllegalStateException(
                    "No se puede cancelar esta vacante: existe al menos una solicitud adjudicada"
                );
        }
        
        //Si no hay adjudicadas: 
        // Se borran todas las solicitudes de esa vacante
        solicitudRepository.deleteByVacanteId(id);

        // Se marca la vacante como CANCELADA y la guardamos
        v.setEstatus(Vacante.Estatus.CANCELADA);
        vacanteRepository.save(v);
    }
	
	
	//Se sobreescribe el método update para indicar que si se modifica el estado a cancelada las solicitudes
	// de esa vacante tambiénse eliminarán:
	
	 @Override
	 @Transactional
	 public Vacante update(Vacante nueva) {
	        // Cargamos la vacante previa de la BBDD
	        Vacante anterior = vacanteRepository.findById(nueva.getIdVacante())
	            .orElseThrow(() -> new EntityNotFoundException("Vacante no encontrada"));

	        // Comporbamos que la vacante anterior no tenga el estado "CANCELADA" y la nueva si lo tenga
	        boolean pasaACancelada = 
	             anterior.getEstatus() != Vacante.Estatus.CANCELADA
	          && nueva.getEstatus() == Vacante.Estatus.CANCELADA;

	        if (pasaACancelada) {
	            // Si lo anterior es true,significa que se ha cambiado el estado a "CANCELADA" y borramos todas las solicitudes asociadas a esa vacante
	            solicitudRepository.deleteByVacanteId(nueva.getIdVacante());
	        }

	        // Guardamos los cambios de la vacante (incluyendo el estatus)
	        return vacanteRepository.save(nueva);
	    }

	 
	 
	/*VALORAR SI ES NECESARIO
	 * @Override
	public List<Vacante> findByEstado(String estado) {
		
		return vacanteRepository.findByEstado(estado);
	}*/

}
