package vacantes_api.modelo.service;

import java.util.List;

import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.dto.RegisterRequestDTO;
import vacantes_api.modelo.dto.UsuarioPasswordDTO;
import vacantes_api.modelo.entity.Usuario;

public interface IUsuarioService extends IGenericoCRUD<Usuario, String> {

    Usuario auth(String email, String password);

    Usuario register(RegisterRequestDTO dto);

    void cambiarEstadoUsuario(String email, Integer nuevoEstado);

    UsuarioPasswordDTO registerEmpresa(EmpresaRegisterRequestDTO dto);

    List<Usuario> findByNombre(String nombre);

    List<Usuario> findByRol(String rol);

    List<Usuario> findByEstado(Integer estado);

}
