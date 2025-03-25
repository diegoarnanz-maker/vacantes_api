package vacantes_api.modelo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.repository.IEmpresaRepository;

@Service
public class EmpresaServiceImplMy8 extends GenericoCRUDServiceImplMy8<Empresa, Integer> implements IEmpresaService {

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Override
    protected IEmpresaRepository getRepository() {
        return empresaRepository;
    }

    @Override
    public Optional<Empresa> findByUsuarioEmail(String email) {
        return empresaRepository.findByUsuarioEmail(email);
    }

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

    @Override
    public void setEstadoUsuarioEmpresa(Integer idEmpresa, Integer estado) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada con id: " + idEmpresa));

        Usuario usuario = empresa.getUsuario();
        usuario.setEnabled(estado);

        empresaRepository.save(empresa);
    }

    

}
