package vacantes_api.modelo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudRequestDTO {

    @NotBlank
    private String archivo;

    @NotBlank
    private String curriculum;

    private String comentarios;

    @NotNull
    private Integer idVacante;
}
