package vacantes_api.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Categoria;

/**
 * Repositorio JPA para la entidad {@link Categoria}.
 * Proporciona operaciones CRUD y consultas personalizadas sobre la tabla de
 * categorías.
 */
public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {

    /**
     * Busca categorías cuyo nombre contenga la cadena indicada, ignorando
     * mayúsculas y minúsculas.
     *
     * @param nombre parte del nombre a buscar.
     * @return lista de categorías coincidentes.
     */
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

}
