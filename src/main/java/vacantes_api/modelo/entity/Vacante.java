package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una vacante de empleo publicada por una empresa.
 * Cada vacante está asociada a una categoría y a una empresa, y puede recibir
 * múltiples solicitudes.
 */
@Entity
@Table(name = "Vacantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Vacante implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la vacante (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacante")
    private Integer idVacante;

    /**
     * Título o nombre de la vacante.
     */
    @Column(nullable = false, length = 200)
    private String nombre;

    /**
     * Descripción completa de la vacante.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Fecha en la que se publica la vacante.
     */
    @Column(nullable = false)
    private LocalDate fecha;

    /**
     * Salario ofrecido para la vacante.
     */
    @Column(nullable = false)
    private Double salario;

    /**
     * Estado actual de la vacante:
     * <ul>
     * <li>CREADA: Vacante disponible para postulación</li>
     * <li>CUBIERTA: Ya ha sido adjudicada</li>
     * <li>CANCELADA: Eliminada o cerrada</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('CREADA','CUBIERTA','CANCELADA')")
    private Estatus estatus;

    /**
     * Indica si la vacante es destacada en la plataforma.
     */
    @Column(nullable = false)
    private Boolean destacado;

    /**
     * URL o nombre del archivo de imagen representativa de la vacante.
     */
    @Column(nullable = false, length = 250)
    private String imagen;

    /**
     * Detalles técnicos u operativos adicionales de la vacante.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String detalles;

    /**
     * Categoría a la que pertenece la vacante.
     */
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    /**
     * Empresa que publica la vacante.
     */
    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    /**
     * Lista de solicitudes asociadas a esta vacante.
     */
    @OneToMany(mappedBy = "vacante")
    private List<Solicitud> solicitudes;

    /**
     * Enumeración de estados posibles para una vacante.
     */
    public enum Estatus {
        CREADA,
        CUBIERTA,
        CANCELADA
    }
}
