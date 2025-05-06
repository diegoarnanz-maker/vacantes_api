package vacantes_api.modelo.service;

import java.util.Optional;

import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.entity.Usuario;

/**
 * Interfaz de servicio para la gestión de entidades {@link Empresa}.
 * Extiende las operaciones CRUD genéricas definidas en {@link IGenericoCRUD}.
 */
public interface IEmpresaService extends IGenericoCRUD<Empresa, Integer> {

    /**
     * Registra una nueva empresa asociada a un usuario con rol EMPRESA.
     *
     * @param dto     datos de registro de la empresa.
     * @param usuario usuario previamente registrado al que se vincula la empresa.
     * @return la empresa creada y persistida.
     */
    Empresa registerEmpresa(EmpresaRegisterRequestDTO dto, Usuario usuario);

    /**
     * Actualiza los datos de una empresa existente.
     *
     * @param id  identificador de la empresa.
     * @param dto nuevos datos a aplicar.
     * @return la empresa actualizada.
     */
    Empresa updateEmpresa(Integer id, EmpresaRegisterRequestDTO dto);

    /**
     * Cambia el estado del usuario asociado a la empresa, permitiendo activarlo o
     * desactivarlo.
     *
     * @param idEmpresa identificador de la empresa.
     * @param estado    nuevo estado del usuario (1 = activo, 0 = inactivo).
     */
    void setEstadoUsuarioEmpresa(Integer idEmpresa, Integer estado);

    /**
     * Busca una empresa por el email del usuario al que está vinculada.
     *
     * @param email email del usuario.
     * @return un {@link Optional} con la empresa correspondiente, si existe.
     */
    Optional<Empresa> findByUsuarioEmail(String email);
}
