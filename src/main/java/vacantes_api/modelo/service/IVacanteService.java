package vacantes_api.modelo.service;

import java.util.List;

import vacantes_api.modelo.entity.Vacante;

/**
 * Interfaz de servicio para la gestión de vacantes.
 * Extiende las operaciones CRUD genéricas definidas en {@link IGenericoCRUD}.
 */
public interface IVacanteService extends IGenericoCRUD<Vacante, Integer> {

    /**
     * Busca vacantes cuyo nombre contenga una cadena específica (ignorando
     * mayúsculas/minúsculas).
     *
     * @param nombre nombre parcial o completo de la vacante.
     * @return lista de vacantes coincidentes.
     */
    List<Vacante> findByNombre(String nombre);

    /**
     * Busca vacantes por el identificador de su categoría.
     *
     * @param idCategoria ID de la categoría.
     * @return lista de vacantes pertenecientes a la categoría.
     */
    List<Vacante> findByCategoriaId(Integer idCategoria);

    /**
     * Busca vacantes por el nombre de su categoría.
     *
     * @param nombre nombre de la categoría.
     * @return lista de vacantes asociadas a dicha categoría.
     */
    List<Vacante> findByCategoriaNombre(String nombre);

    /**
     * Busca vacantes por el nombre de la empresa que las publica.
     *
     * @param nombreEmpresa nombre de la empresa.
     * @return lista de vacantes publicadas por dicha empresa.
     */
    List<Vacante> findByEmpresaNombreEmpresa(String nombreEmpresa);

    /**
     * Busca vacantes cuyo salario sea mayor o igual al valor especificado.
     *
     * @param salario salario mínimo a filtrar.
     * @return lista de vacantes que cumplen con el criterio salarial.
     */
    List<Vacante> findBySalario(Double salario);

    /*
     * Método para búsqueda por estado de la vacante (opcional).
     * List<Vacante> findByEstado(String estado);
     */
}
