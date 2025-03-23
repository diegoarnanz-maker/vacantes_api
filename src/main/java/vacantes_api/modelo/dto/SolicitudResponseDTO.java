package vacantes_api.modelo.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudResponseDTO {

    private Integer idSolicitud;

    private LocalDate fecha;

    private String archivo;

    private String curriculum;

    private String comentarios;

    private Integer estado;

    // Información de la vacante
    private Integer idVacante;
    private String nombreVacante;

    // Información del usuario solicitante
    private String emailUsuario;
    private String nombreUsuario;
}
