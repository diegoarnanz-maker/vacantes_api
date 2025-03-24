package vacantes_api.modelo.dto;

import lombok.*;
import vacantes_api.modelo.entity.Usuario;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPasswordDTO {
    private Usuario usuario;
    private String passwordGenerada;
}