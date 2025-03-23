package vacantes_api.modelo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;

public interface ISolicitudRepository extends JpaRepository<Solicitud, Integer> {
    Optional<Solicitud> findByVacanteAndUsuario(Vacante vacante, Usuario usuario);

    List<Solicitud> findByUsuarioEmail(String email);

    List<Solicitud> findByVacante(Vacante vacante);

}
