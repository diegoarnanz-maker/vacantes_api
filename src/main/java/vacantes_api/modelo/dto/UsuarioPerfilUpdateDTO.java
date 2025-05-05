package vacantes_api.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPerfilUpdateDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;
}

