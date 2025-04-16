package vacantes_api.modelo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.entity.Vacante;
import vacantes_api.modelo.repository.IVacanteRepository;

@Service
public class VacanteServiceImplMy8 extends GenericoCRUDServiceImplMy8<Vacante, Integer> implements IVacanteService {

    @Autowired
    private IVacanteRepository vacanteRepository;

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

    @Override
    public List<Vacante> findBySalario(Double salario) {
        return vacanteRepository.findBySalarioGreaterThanEqual(salario);
    }

}
