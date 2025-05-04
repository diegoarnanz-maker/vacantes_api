package vacantes_api.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vacantes_api.modelo.dto.EmpresaResponseDTO;
import vacantes_api.modelo.dto.SolicitudResponseDTO;
import vacantes_api.modelo.dto.VacanteResponseDTO;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Vacante;

// ModelMapperConfig.java
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapeo personalizado de Vacante → VacanteResponseDTO
        modelMapper.addMappings(new PropertyMap<Vacante, VacanteResponseDTO>() {
            @Override
            protected void configure() {
                map().setIdCategoria(source.getCategoria().getIdCategoria());
                map().setNombreCategoria(source.getCategoria().getNombre());

                map().setIdEmpresa(source.getEmpresa().getIdEmpresa());
                map().setNombreEmpresa(source.getEmpresa().getNombreEmpresa());
                map().setPais(source.getEmpresa().getPais());
            }
        });

        // Mapeo personalizado de Solicitud → SolicitudResponseDTO
        modelMapper.addMappings(new PropertyMap<Solicitud, SolicitudResponseDTO>() {
            @Override
            protected void configure() {
                map().setIdVacante(source.getVacante().getIdVacante());
                map().setNombreVacante(source.getVacante().getNombre());
                map().setDescripcionVacante(source.getVacante().getDescripcion());
                map().setSalarioVacante(source.getVacante().getSalario());
                map().setDetalleVacante(source.getVacante().getDetalles());
                map().setImagenVacante(source.getVacante().getImagen());
                map().setNombreEmpresa(source.getVacante().getEmpresa().getNombreEmpresa());
                map().setCategoriaVacante(source.getVacante().getCategoria().getNombre());

                map().setEmailUsuario(source.getUsuario().getEmail());
                map().setNombreUsuario(source.getUsuario().getNombre());
                map().setApellidosUsuario(source.getUsuario().getApellidos());
            }
        });

        // Empresa → EmpresaResponseDTO
        modelMapper.addMappings(new PropertyMap<Empresa, EmpresaResponseDTO>() {
            @Override
            protected void configure() {
                map().setEmail(source.getUsuario().getEmail());
                map().setNombre(source.getUsuario().getNombre());
                map().setApellidos(source.getUsuario().getApellidos());
            }
        });

        return modelMapper;
    }
}
