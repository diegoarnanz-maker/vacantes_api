package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa a una empresa dentro del sistema.
 * Cada empresa está asociada a un usuario con rol específico y puede publicar
 * múltiples vacantes.
 */
@Entity
@Table(name = "Empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la empresa (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    /**
     * Código de Identificación Fiscal (CIF) de la empresa.
     * Debe ser único y tener una longitud máxima de 10 caracteres.
     */
    @Column(nullable = false, unique = true, length = 10)
    private String cif;

    /**
     * Nombre oficial de la empresa.
     */
    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    /**
     * Dirección fiscal registrada de la empresa.
     */
    @Column(name = "direccion_fiscal", length = 100)
    private String direccionFiscal;

    /**
     * País de residencia fiscal de la empresa.
     */
    @Column(length = 45)
    private String pais;

    /**
     * Usuario asociado a la empresa. Se establece una relación uno a uno.
     * El campo "email" en esta tabla actúa como clave foránea hacia la tabla de
     * usuarios.
     */
    @OneToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    @ToString.Exclude
    private Usuario usuario;

    /**
     * Lista de vacantes publicadas por la empresa.
     * Relación uno a muchos gestionada desde la entidad {@link Vacante}.
     */
    @OneToMany(mappedBy = "empresa")
    private List<Vacante> vacantes;
}
