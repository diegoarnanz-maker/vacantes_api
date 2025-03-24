package vacantes_api.modelo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaResponseDTO {

    private Integer idEmpresa;

    private String cif;

    private String nombreEmpresa;

    private String direccionFiscal;

    private String pais;

    // Datos del usuario asociado (rol EMPRESA)
    private String email;
    private String nombre;
    private String apellidos;

    // Datos de vacante
    @Builder.Default
    private List<VacanteResponseDTO> vacantes = new ArrayList<>();
}
