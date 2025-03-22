package vacantes_api.modelo.service;

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

}
