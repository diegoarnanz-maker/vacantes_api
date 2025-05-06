package vacantes_api.modelo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.repository.IEmpresaRepository;

/**
 * Implementación del servicio de gestión de empresas.
 * Extiende la clase genérica {@link GenericoCRUDServiceImplMy8} para
 * operaciones básicas,
 * y añade funcionalidades específicas como registro, actualización y gestión de
 * estado.
 */
@Service
public class EmpresaServiceImplMy8 extends GenericoCRUDServiceImplMy8<Empresa, Integer> implements IEmpresaService {

    @Autowired
    private IEmpresaRepository empresaRepository;

    /**
     * Retorna el repositorio específico para la entidad {@link Empresa}.
     *
     * @return repositorio de empresa.
     */
    @Override
    protected IEmpresaRepository getRepository() {
        return empresaRepository;
    }

    /**
     * Busca una empresa por el email del usuario asociado.
     *
     * @param email email del usuario.
     * @return empresa correspondiente, si existe.
     */
    @Override
    public Optional<Empresa> findByUsuarioEmail(String email) {
        return empresaRepository.findByUsuarioEmail(email);
    }

    /**
     * Registra una nueva empresa asociada a un usuario ya existente.
     *
     * @param dto     datos de la empresa.
     * @param usuario usuario asociado con rol EMPRESA.
     * @return empresa guardada en la base de datos.
     */
    @Override
    public Empresa registerEmpresa(EmpresaRegisterRequestDTO dto, Usuario usuario) {
        Empresa empresa = Empresa.builder()
                .cif(dto.getCif())
                .nombreEmpresa(dto.getNombreEmpresa())
                .direccionFiscal(dto.getDireccionFiscal())
                .pais(dto.getPais())
                .usuario(usuario)
                .build();

        return empresaRepository.save(empresa);
    }

    /**
     * Actualiza los datos de una empresa existente.
     *
     * @param id  ID de la empresa a actualizar.
     * @param dto datos nuevos de la empresa.
     * @return empresa actualizada.
     */
    @Override
    public Empresa updateEmpresa(Integer id, EmpresaRegisterRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada con id: " + id));

        empresa.setCif(dto.getCif());
        empresa.setNombreEmpresa(dto.getNombreEmpresa());
        empresa.setDireccionFiscal(dto.getDireccionFiscal());
        empresa.setPais(dto.getPais());

        return empresaRepository.save(empresa);
    }

    /**
     * Activa o desactiva el usuario asociado a una empresa, modificando el campo
     * {@code enabled}.
     *
     * @param idEmpresa ID de la empresa.
     * @param estado    nuevo estado del usuario (1 = activo, 0 = inactivo).
     */
    @Override
    public void setEstadoUsuarioEmpresa(Integer idEmpresa, Integer estado) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada con id: " + idEmpresa));

        Usuario usuario = empresa.getUsuario();
        usuario.setEnabled(estado);

        empresaRepository.save(empresa);
    }
}
