package vacantes_api.modelo.service;

import java.util.List;

import vacantes_api.modelo.entity.Vacante;

public interface IVacanteService extends IGenericoCRUD<Vacante, Integer> {


    List<Vacante> findByNombre(String nombre);
    
    List<Vacante> findByCategoriaId(Integer idCategoria);
    
    //Añadidos:
    
    List<Vacante> findByCategoriaNombre(String nombre);
    
    List<Vacante> findByEmpresaNombreEmpresa(String nombreEmpresa);

    List<Vacante> findBySalario(Double salario);
    
    /*List<Vacante> findByEstado(String estado);  VALORAR SI ES NECESARIO*/ 

}
