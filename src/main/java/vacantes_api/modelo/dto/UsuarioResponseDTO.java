package vacantes_api.modelo.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private String email;
    private String nombre;
    private String apellidos;
    private String rol;
    private Integer enabled;
    private LocalDate fechaRegistro;
    
    private String nombreEmpresa;
    private String cifEmpresa;
    private String direccionFiscal;
    private String paisEmpresa;

}
