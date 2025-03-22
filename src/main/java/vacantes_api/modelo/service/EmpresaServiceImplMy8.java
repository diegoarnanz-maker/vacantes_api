package vacantes_api.modelo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.repository.IEmpresaRepository;

@Service
public class EmpresaServiceImplMy8 extends GenericoCRUDServiceImplMy8<Empresa, Integer> implements IEmpresaService {

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Override
    protected IEmpresaRepository getRepository() {
        return empresaRepository;
    }

}
