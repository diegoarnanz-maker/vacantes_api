package vacantes_api.modelo.service;

import java.util.List;

import vacantes_api.modelo.entity.Vacante;

public interface IVacanteService extends IGenericoCRUD<Vacante, Integer> {

    //Es complejo, habra que hablar que logica necesitamos (filtrar por estado, empresa, etc).

    List<Vacante> findByNombre(String nombre);
    
    List<Vacante> findByCategoriaId(Integer idCategoria);

}
