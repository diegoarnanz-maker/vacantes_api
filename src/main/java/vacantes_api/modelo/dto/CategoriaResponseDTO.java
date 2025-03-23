package vacantes_api.modelo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResponseDTO {
    // En el futuro si se necesita una lista de vacantes se podria hacer un
    // CategoriaDetellaDTO mas adelante

    private Integer idCategoria;
    private String nombre;
    private String descripcion;
}
