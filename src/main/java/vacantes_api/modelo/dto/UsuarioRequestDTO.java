package vacantes_api.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    private String rol;

    private Integer enabled;

    // Contraseña opcional (sin validación de @Size ni @NotBlank)
    private String password;
}
