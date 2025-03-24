package vacantes_api.modelo.service;

import java.util.Optional;

import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.entity.Usuario;

public interface IEmpresaService extends IGenericoCRUD<Empresa, Integer> {

    Empresa registerEmpresa(EmpresaRegisterRequestDTO dto, Usuario usuario);

    Empresa updateEmpresa(Integer id, EmpresaRegisterRequestDTO dto);

    void deleteEmpresa(Integer id);

    Optional<Empresa> findByUsuarioEmail(String email);

}
