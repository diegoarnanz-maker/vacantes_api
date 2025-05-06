package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa a un usuario del sistema.
 * Implementa la interfaz {@link UserDetails} para integrarse con Spring Security.
 * Un usuario puede tener diferentes roles, solicitudes y estar vinculado a una empresa.
 */
@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * Email del usuario. Actúa como clave primaria y nombre de usuario.
     */
    @Id
    @Column(length = 45)
    private String email;

    /**
     * Nombre del usuario.
     */
    @Column(nullable = false, length = 45)
    private String nombre;

    /**
     * Apellidos del usuario.
     */
    @Column(nullable = false, length = 100)
    private String apellidos;

    /**
     * Contraseña del usuario, codificada.
     */
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * Indica si la cuenta está activa (1) o desactivada (0).
     */
    @Column(nullable = false)
    private Integer enabled = 1;

    /**
     * Fecha en la que se registró el usuario.
     */
    @Column(name = "fecha_Registro")
    private LocalDate fechaRegistro;

    /**
     * Rol asignado al usuario (por ejemplo: ADMIN, EMPRESA, CLIENTE).
     */
    @Column(nullable = false, length = 15)
    private String rol;

    /**
     * Empresa asociada al usuario (solo para usuarios con rol EMPRESA).
     * Relación uno a uno inversa. La clave foránea está en la entidad Empresa.
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Empresa empresa;

    /**
     * Lista de solicitudes realizadas por el usuario.
     */
    @OneToMany(mappedBy = "usuario")
    private List<Solicitud> solicitudes;

    /**
     * Devuelve la colección de roles de Spring Security con prefijo 'ROLE_'.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + rol);
    }

    /**
     * Devuelve el email como nombre de usuario para autenticación.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Devuelve la contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return password;
    }
}
