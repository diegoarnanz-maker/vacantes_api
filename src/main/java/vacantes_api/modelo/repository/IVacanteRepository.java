package vacantes_api.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Vacante;

public interface IVacanteRepository extends JpaRepository<Vacante, Integer> {

    List<Vacante> findByNombreContainingIgnoreCase(String nombre);

    List<Vacante> findByCategoriaIdCategoria(Integer idCategoria);
    
    //Añadidos:
    
    List<Vacante> findByCategoriaNombreContainingIgnoreCase(String nombre);
    
    List<Vacante> findByEmpresaNombreEmpresaContainingIgnoreCase(String nombreEmpresa);

    List<Vacante> findBySalarioGreaterThanEqual(Double salario);
    
   /* List<Vacante> findByEstado(String estado); VALORAR SI ES NECESARIO*/

}
