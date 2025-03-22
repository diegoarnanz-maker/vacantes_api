package vacantes_api.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, String> {

}
