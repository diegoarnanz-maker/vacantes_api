package vacantes_api.modelo.service;

import java.util.Optional;

import vacantes_api.modelo.entity.Categoria;

public interface ICategoriaService extends IGenericoCRUD<Categoria, Integer> {

    Optional<Categoria> findByName(String name);
    
}
