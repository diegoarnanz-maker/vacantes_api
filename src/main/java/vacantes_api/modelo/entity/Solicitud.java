package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Solicitudes", uniqueConstraints = @UniqueConstraint(columnNames = { "id_Vacante", "email" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 250)
    private String archivo;

    @Column(length = 2000)
    private String comentarios;

    @Builder.Default //Permite que el valor por defecto de estado sea 0, si no se indica el valor por defecto será null 
    @Column(nullable = false)
    private Integer estado = 0;  
    
    @Column(length = 45)
    private String curriculum;

    @ManyToOne
    @JoinColumn(name = "id_Vacante", nullable = false)
    private Vacante vacante;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Usuario usuario;
}
