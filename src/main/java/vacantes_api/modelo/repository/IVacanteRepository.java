package vacantes_api.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Vacante;

public interface IVacanteRepository extends JpaRepository<Vacante, Integer> {

}
