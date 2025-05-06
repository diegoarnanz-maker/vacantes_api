package vacantes_api.config;

import static org.modelmapper.Conditions.isNotNull;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vacantes_api.modelo.dto.EmpresaResponseDTO;
import vacantes_api.modelo.dto.SolicitudResponseDTO;
import vacantes_api.modelo.dto.UsuarioResponseDTO;
import vacantes_api.modelo.dto.VacanteResponseDTO;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;

/**
 * Clase de configuración de ModelMapper.
 * Define los mapeos personalizados entre entidades y DTOs del sistema.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Define el bean de {@link ModelMapper} con las reglas de mapeo personalizadas.
     *
     * @return instancia de ModelMapper con configuraciones específicas.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        /**
         * Mapeo personalizado de Vacante → VacanteResponseDTO.
         * Incluye campos de la entidad asociada Empresa y Categoria.
         */
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

        /**
         * Mapeo personalizado de Solicitud → SolicitudResponseDTO.
         * Incluye campos de la vacante y del usuario relacionados.
         */
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

        /**
         * Mapeo personalizado de Empresa → EmpresaResponseDTO.
         * Añade datos del usuario asociado a la empresa.
         */
        modelMapper.addMappings(new PropertyMap<Empresa, EmpresaResponseDTO>() {
            @Override
            protected void configure() {
                map().setEmail(source.getUsuario().getEmail());
                map().setNombre(source.getUsuario().getNombre());
                map().setApellidos(source.getUsuario().getApellidos());
            }
        });

        /**
         * Mapeo personalizado de Usuario → UsuarioResponseDTO.
         * Incluye información de la empresa si está disponible.
         */
        modelMapper.addMappings(new PropertyMap<Usuario, UsuarioResponseDTO>() {
            @Override
            protected void configure() {
                when(isNotNull()).map(source.getEmpresa().getNombreEmpresa(), destination.getNombreEmpresa());
                when(isNotNull()).map(source.getEmpresa().getCif(), destination.getCifEmpresa());
                when(isNotNull()).map(source.getEmpresa().getDireccionFiscal(), destination.getDireccionFiscal());
                when(isNotNull()).map(source.getEmpresa().getPais(), destination.getPaisEmpresa());
            }
        });

        return modelMapper;
    }
}
