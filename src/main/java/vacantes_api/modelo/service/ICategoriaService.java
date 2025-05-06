package vacantes_api.modelo.service;

import java.util.List;

import vacantes_api.modelo.entity.Categoria;

/**
 * Interfaz de servicio para la gestión de categorías.
 * Extiende las operaciones CRUD genéricas definidas en {@link IGenericoCRUD}.
 */
public interface ICategoriaService extends IGenericoCRUD<Categoria, Integer> {

    /**
     * Busca categorías cuyo nombre contenga una cadena específica (ignorando
     * mayúsculas/minúsculas).
     *
     * @param name nombre parcial o completo de la categoría a buscar.
     * @return lista de categorías coincidentes.
     */
    List<Categoria> findByName(String name);
}
