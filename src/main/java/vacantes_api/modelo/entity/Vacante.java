package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Vacantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacante")
    private Integer idVacante;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Double salario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = false, columnDefinition = "ENUM('CREADA','CUBIERTA','CANCELADA')")
    private Estatus estatus;

    @Column(nullable = false)
    private Boolean destacado;

    @Column(nullable = false, length = 250)
    private String imagen;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String detalles;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @OneToMany(mappedBy = "vacante")
    private List<Solicitud> solicitudes;

    public enum Estatus {
        CREADA,
        CUBIERTA,
        CANCELADA
    }
    
}


