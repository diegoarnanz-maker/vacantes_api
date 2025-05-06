package vacantes_api.modelo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.entity.Categoria;
import vacantes_api.modelo.repository.ICategoriaRepository;

/**
 * Implementación del servicio de gestión de categorías.
 * Extiende la clase base {@link GenericoCRUDServiceImplMy8} para operaciones
 * CRUD genéricas
 * y añade lógica específica para buscar por nombre.
 */
@Service
public class CategoriaServiceImplMy8 extends GenericoCRUDServiceImplMy8<Categoria, Integer>
        implements ICategoriaService {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    /**
     * Devuelve el repositorio asociado a la entidad {@link Categoria}.
     *
     * @return repositorio de categoría.
     */
    @Override
    protected ICategoriaRepository getRepository() {
        return categoriaRepository;
    }

    /**
     * Busca categorías por nombre (insensible a mayúsculas).
     *
     * @param name nombre parcial o completo de la categoría.
     * @return lista de categorías coincidentes.
     */
    @Override
    public List<Categoria> findByName(String name) {
        return categoriaRepository.findByNombreContainingIgnoreCase(name);
    }
}
