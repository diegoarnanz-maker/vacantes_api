package vacantes_api.modelo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

    private String email;
    private String nombre;
    private String rol;
}
