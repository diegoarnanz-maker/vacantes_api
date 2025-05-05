package vacantes_api.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaPerfilUpdateDTO {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;

    @NotBlank(message = "El CIF es obligatorio")
    private String cif;

    @NotBlank(message = "La dirección fiscal es obligatoria")
    private String direccionFiscal;

    @NotBlank(message = "El país es obligatorio")
    private String pais;
}
