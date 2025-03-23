package vacantes_api.modelo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    @Email
    private String email;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
    
}
