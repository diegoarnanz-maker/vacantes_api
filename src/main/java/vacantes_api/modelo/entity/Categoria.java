package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una categoría dentro del sistema.
 * Cada categoría puede estar asociada a múltiples vacantes.
 */
@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la categoría (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    /**
     * Nombre de la categoría. No puede ser nulo y tiene un límite de 100
     * caracteres.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Descripción detallada de la categoría (hasta 2000 caracteres).
     */
    @Column(length = 2000)
    private String descripcion;

    /**
     * Lista de vacantes asociadas a esta categoría.
     * Relación bidireccional uno a muchos.
     */
    @OneToMany(mappedBy = "categoria")
    private List<Vacante> vacantes;
}
