package vacantes_api.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, String> {
    
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    List<Usuario> findByRol(String rol);

    List<Usuario> findByEnabled(Integer enabled);
    
}
