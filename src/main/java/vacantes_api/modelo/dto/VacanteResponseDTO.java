package vacantes_api.modelo.dto;

import java.time.LocalDate;

import lombok.*;
import vacantes_api.modelo.entity.Vacante.Estatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacanteResponseDTO {

    private Integer idVacante;

    private String nombre;

    private String descripcion;

    private LocalDate fecha;

    private Double salario;

    private Estatus estatus;

    private Boolean destacado;

    private String imagen;

    private String detalles;

    // Traemos id y nombre para faacilitar el trabajo en el frontend
    private Integer idCategoria;
    private String nombreCategoria;

    private Integer idEmpresa;
    private String nombreEmpresa;
    private String pais;
}
