package vacantes_api.modelo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;

public interface ISolicitudRepository extends JpaRepository<Solicitud, Integer> {
	
	
    Optional<Solicitud> findByVacanteAndUsuario(Vacante vacante, Usuario usuario);

    List<Solicitud> findByUsuarioEmail(String email);

    List<Solicitud> findByVacante(Vacante vacante);
    
    //Se añade una Query para eliminar las solicitudes que estén vinculadas a una vacante:
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Solicitud s WHERE s.vacante.idVacante= ?1")
    void deleteByVacanteId(Integer vacanteId);
    
    /*	@Query define la sentencia JPQL de borrado.

		@Modifying le dice a Spring Data que se trata de una operación de modificación.

		@Transactional asegura que la operación se ejecute dentro de una transacción.
		
		La Query sería lo mismo que esto:
		
		@Query("DELETE FROM Solicitud s WHERE s.vacante.idVacante = :vacanteId")
		void deleteByVacanteId(@Param("vacanteId") Integer id);
		
		Pero en vez de usar nombres específicos con el @Param se usa ?1, que es un marcador posicional que indica que
		el WHERE se comparará con el valor del primer parámetro que se pase por el método deleteByVacante. 
		*/

    
   //Se añade una Query para que cuando se adjudique una vacante a una solicitud, las otras solicitudes pasen a estado=2 (rechazada)
    
    /*WHERE s.vacante.idVacante = :vacanteId: sólo sobre aquellas solicitudes que pertenecen a la vacante con ID vacanteId.
	AND s.idSolicitud <> :ganadoraId: y además solo las solicitudes que no tengan el id de la solicitud seleccionada(la que se ha adjudicado).*/
    @Modifying
    @Transactional
    @Query("UPDATE Solicitud s SET s.estado = 2 WHERE s.vacante.idVacante = :vacanteId AND s.idSolicitud <> :seleccionadaId")
    void rejectOthers (@Param("vacanteId") Integer vacanteId,
    					@Param("seleccionadaId")  Integer seleccionadaId
    					);


   //Se añade una Query para poner a estado= 0 (pendiente de valorar) todas las solicitudes de una vacante id:
    @Modifying
    @Transactional
    @Query("UPDATE Solicitud s SET s.estado = 0 WHERE s.vacante.idVacante = ?1")
    void resetAllEstado(Integer vacanteId);
    
    
   //Se añade un método derivado que comprueba si existe al menos una solicitud con el estado que indiquemos para esa vacante:
    
    boolean existsByVacanteIdVacanteAndEstado(Integer vacanteId, Integer estado);
}
