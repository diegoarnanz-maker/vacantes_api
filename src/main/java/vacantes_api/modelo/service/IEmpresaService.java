package vacantes_api.modelo.service;

import java.util.Optional;

import vacantes_api.modelo.entity.Empresa;

public interface IEmpresaService extends IGenericoCRUD<Empresa, Integer> {

    Optional<Empresa> findByUsuarioEmail(String email);

}
