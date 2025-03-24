package vacantes_api.modelo.service;

import java.util.List;
import java.util.Optional;

import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.dto.RegisterRequestDTO;
import vacantes_api.modelo.dto.UsuarioPasswordDTO;
import vacantes_api.modelo.entity.Usuario;

public interface IUsuarioService extends IGenericoCRUD<Usuario, String> {

    Usuario auth(String email, String password);

    Usuario register(RegisterRequestDTO dto);

    UsuarioPasswordDTO registerEmpresa(EmpresaRegisterRequestDTO dto);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRol(String rol);

}
