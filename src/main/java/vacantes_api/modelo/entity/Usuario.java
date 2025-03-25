package vacantes_api.modelo.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 45)
    private String email;

    @Column(nullable = false, length = 45)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private Integer enabled = 1;

    @Column(name = "fecha_Registro")
    private LocalDate fechaRegistro;

    @Column(nullable = false, length = 15)
    private String rol;

    // Un usuario con rol EMPRESA tiene asociada una única empresa.
    // Relación inversa del @OneToOne que hay en la entidad Empresa.
    // La clave foránea está en la tabla Empresas (email), no en Usuarios.
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Empresa empresa;

    // Un usuario puede tener muchas solicitudes (relación 1:N).
    // El lado "dueño" de la relación esta en la clase Solicitud como usuario.
    @OneToMany(mappedBy = "usuario")
    private List<Solicitud> solicitudes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + rol);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
