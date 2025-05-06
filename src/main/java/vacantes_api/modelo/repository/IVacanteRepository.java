package vacantes_api.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Vacante;

/**
 * Repositorio JPA para la entidad {@link Vacante}.
 * Permite realizar operaciones CRUD y consultas personalizadas sobre vacantes
 * de empleo.
 */
public interface IVacanteRepository extends JpaRepository<Vacante, Integer> {

    /**
     * Busca vacantes cuyo nombre contenga una cadena específica (ignorando
     * mayúsculas/minúsculas).
     *
     * @param nombre nombre parcial a buscar.
     * @return lista de vacantes coincidentes.
     */
    List<Vacante> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca vacantes por ID de categoría.
     *
     * @param idCategoria identificador de la categoría.
     * @return lista de vacantes asociadas a dicha categoría.
     */
    List<Vacante> findByCategoriaIdCategoria(Integer idCategoria);

    /**
     * Busca vacantes por nombre de la categoría (coincidencia parcial e insensible
     * a mayúsculas).
     *
     * @param nombre nombre de la categoría.
     * @return lista de vacantes cuya categoría coincida.
     */
    List<Vacante> findByCategoriaNombreContainingIgnoreCase(String nombre);

    /**
     * Busca vacantes por nombre de empresa (coincidencia parcial e insensible a
     * mayúsculas).
     *
     * @param nombreEmpresa nombre parcial de la empresa.
     * @return lista de vacantes publicadas por empresas coincidentes.
     */
    List<Vacante> findByEmpresaNombreEmpresaContainingIgnoreCase(String nombreEmpresa);

    /**
     * Busca vacantes cuyo salario sea mayor o igual al indicado.
     *
     * @param salario salario mínimo a considerar.
     * @return lista de vacantes que cumplen con la condición.
     */
    List<Vacante> findBySalarioGreaterThanEqual(Double salario);

    /*
     * // Método alternativo a valorar si se desea buscar por estado textual.
     * List<Vacante> findByEstado(String estado);
     */
}
