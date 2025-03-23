package vacantes_api.modelo.service;

import java.util.List;

import vacantes_api.modelo.entity.Categoria;

public interface ICategoriaService extends IGenericoCRUD<Categoria, Integer> {

    List<Categoria> findByName(String name);

}
