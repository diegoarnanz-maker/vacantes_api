package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    // Declarar la relación uno a muchos (1:N) entre una categoría y sus vacantes
    @OneToMany(mappedBy = "categoria")
    private List<Vacante> vacantes;
    
}
