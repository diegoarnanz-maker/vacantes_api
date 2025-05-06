package vacantes_api.modelo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Empresa;

/**
 * Repositorio JPA para la entidad {@link Empresa}.
 * Permite realizar operaciones CRUD y consultas personalizadas sobre empresas.
 */
public interface IEmpresaRepository extends JpaRepository<Empresa, Integer> {

    /**
     * Busca una empresa por el email del usuario asociado.
     *
     * @param email email del usuario vinculado a la empresa.
     * @return una {@code Optional<Empresa>} si existe una coincidencia.
     */
    Optional<Empresa> findByUsuarioEmail(String email);
}
