package vacantes_api.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Usuario;

/**
 * Repositorio JPA para la entidad {@link Usuario}.
 * Proporciona operaciones CRUD y consultas personalizadas sobre usuarios del
 * sistema.
 */
public interface IUsuarioRepository extends JpaRepository<Usuario, String> {

    /**
     * Busca usuarios cuyo nombre contenga la cadena indicada, ignorando mayúsculas
     * y minúsculas.
     *
     * @param nombre parte del nombre a buscar.
     * @return lista de usuarios coincidentes.
     */
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca usuarios según su rol exacto (por ejemplo: ADMIN, EMPRESA, CLIENTE).
     *
     * @param rol rol del usuario.
     * @return lista de usuarios con ese rol.
     */
    List<Usuario> findByRol(String rol);

    /**
     * Busca usuarios según su estado de activación (1 = activo, 0 = inactivo).
     *
     * @param enabled estado del usuario.
     * @return lista de usuarios con ese estado.
     */
    List<Usuario> findByEnabled(Integer enabled);
}
