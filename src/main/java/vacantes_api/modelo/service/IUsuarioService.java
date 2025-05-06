package vacantes_api.modelo.service;

import java.util.List;

import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.dto.RegisterRequestDTO;
import vacantes_api.modelo.dto.UsuarioPasswordDTO;
import vacantes_api.modelo.entity.Usuario;

/**
 * Interfaz de servicio para la gestión de usuarios.
 * Extiende las operaciones CRUD genéricas definidas en {@link IGenericoCRUD}.
 */
public interface IUsuarioService extends IGenericoCRUD<Usuario, String> {

    /**
     * Autentica un usuario mediante su email y contraseña.
     *
     * @param email    email del usuario.
     * @param password contraseña sin encriptar.
     * @return usuario autenticado si las credenciales son válidas.
     */
    Usuario auth(String email, String password);

    /**
     * Registra un nuevo usuario con rol CLIENTE.
     *
     * @param dto datos de registro del usuario.
     * @return usuario registrado.
     */
    Usuario register(RegisterRequestDTO dto);

    /**
     * Cambia el estado (activo/inactivo) del usuario.
     *
     * @param email       email del usuario.
     * @param nuevoEstado nuevo estado (1 = activo, 0 = inactivo).
     */
    void cambiarEstadoUsuario(String email, Integer nuevoEstado);

    /**
     * Registra un nuevo usuario con rol EMPRESA, y devuelve la información
     * necesaria para su empresa asociada.
     *
     * @param dto datos de la empresa y del usuario.
     * @return objeto con el usuario creado y la contraseña generada.
     */
    UsuarioPasswordDTO registerEmpresa(EmpresaRegisterRequestDTO dto);

    /**
     * Busca un usuario por su email.
     *
     * @param email email del usuario.
     * @return usuario correspondiente.
     */
    Usuario findByEmail(String email);

    /**
     * Busca usuarios cuyo nombre contenga la cadena dada (ignorando
     * mayúsculas/minúsculas).
     *
     * @param nombre nombre a buscar.
     * @return lista de usuarios coincidentes.
     */
    List<Usuario> findByNombre(String nombre);

    /**
     * Busca usuarios por su rol.
     *
     * @param rol rol a filtrar (ej: ADMIN, CLIENTE, EMPRESA).
     * @return lista de usuarios con el rol especificado.
     */
    List<Usuario> findByRol(String rol);

    /**
     * Busca usuarios por su estado (activo o inactivo).
     *
     * @param estado estado del usuario (1 = activo, 0 = inactivo).
     * @return lista de usuarios según el estado.
     */
    List<Usuario> findByEstado(Integer estado);
}
