package vacantes_api.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Solicitud;

public interface ISolicitudRepository extends JpaRepository<Solicitud, Integer> {

}
