package vacantes_api.modelo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.*;
import vacantes_api.modelo.entity.Vacante.Estatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacanteRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String descripcion;

    @NotNull
    private LocalDate fecha;

    @NotNull
    @Positive
    private Double salario;

    @NotNull
    private Estatus estatus;

    @NotNull
    private Boolean destacado;

    @NotBlank
    private String imagen;

    @NotBlank
    private String detalles;

    //Habra que ver si es mejor que el cliente meta el id o el nombre cuando montemos el frontend
    @NotNull
    private Integer idCategoria;

    @NotNull
    private Integer idEmpresa;
}
