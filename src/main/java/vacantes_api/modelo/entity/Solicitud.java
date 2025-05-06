package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una solicitud de un usuario para una vacante.
 * Cada solicitud está vinculada a una vacante y a un usuario específico.
 * No se permite que un usuario postule dos veces a la misma vacante.
 */
@Entity
@Table(name = "Solicitudes", uniqueConstraints = @UniqueConstraint(columnNames = { "id_Vacante", "email" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la solicitud (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    /**
     * Fecha en la que se realiza la solicitud.
     */
    @Column(nullable = false)
    private LocalDate fecha;

    /**
     * Ruta o identificador del archivo adjunto (por ejemplo, carta de
     * presentación).
     */
    @Column(nullable = false, length = 250)
    private String archivo;

    /**
     * Comentarios adicionales proporcionados por el candidato.
     */
    @Column(length = 2000)
    private String comentarios;

    /**
     * Estado de la solicitud:
     * <ul>
     * <li>0: Enviada</li>
     * <li>1: Adjudicada</li>
     * <li>2: Rechazada</li>
     * </ul>
     * Valor por defecto: 0 (enviada).
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer estado = 0;

    /**
     * Ruta o identificador del CV cargado por el usuario.
     */
    @Column(length = 45)
    private String curriculum;

    /**
     * Vacante a la que se postula.
     */
    @ManyToOne
    @JoinColumn(name = "id_Vacante", nullable = false)
    private Vacante vacante;

    /**
     * Usuario que realiza la solicitud.
     */
    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Usuario usuario;
}
