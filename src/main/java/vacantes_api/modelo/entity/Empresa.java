package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(nullable = false, unique = true, length = 10)
    private String cif;

    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(name = "direccion_fiscal", length = 100)
    private String direccionFiscal;

    @Column(length = 45)
    private String pais;

    // Una empresa está asociada a un único usuario (con rol EMPRESA)
    // El campo email en la tabla Empresas es una clave foránea que apunta al campo
    // email en la tabla Usuarios.
    @OneToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    @ToString.Exclude
    private Usuario usuario;

    // Una empresa puede tener muchas vacantes.
    // la propiedad empresa está definida en la entidad Vacante, y ahí es donde se
    // gestiona la clave foránea.
    @OneToMany(mappedBy = "empresa")
    private List<Vacante> vacantes;
}
