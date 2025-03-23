package vacantes_api.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vacantes_api.modelo.dto.VacanteResponseDTO;
import vacantes_api.modelo.entity.Vacante;

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
            }
        });

        return modelMapper;
    }
}
