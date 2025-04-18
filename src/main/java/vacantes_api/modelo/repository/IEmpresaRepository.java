package vacantes_api.modelo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Empresa;

public interface IEmpresaRepository extends JpaRepository<Empresa, Integer> {

    Optional<Empresa> findByUsuarioEmail(String email);

}
