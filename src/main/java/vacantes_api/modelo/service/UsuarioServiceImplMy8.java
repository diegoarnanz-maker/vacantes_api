package vacantes_api.modelo.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.dto.RegisterRequestDTO;
import vacantes_api.modelo.dto.UsuarioPasswordDTO;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.repository.IUsuarioRepository;

/**
 * Implementación del servicio {@link IUsuarioService} que gestiona la lógica
 * relacionada con la autenticación, registro y gestión de usuarios en la
 * aplicación.
 * También implementa {@link UserDetailsService} para la integración con Spring
 * Security.
 */
@Service
public class UsuarioServiceImplMy8 extends GenericoCRUDServiceImplMy8<Usuario, String>
        implements IUsuarioService, UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected IUsuarioRepository getRepository() {
        return usuarioRepository;
    }

    /**
     * Genera una contraseña aleatoria segura con una longitud especificada.
     * 
     * @param longitud número de caracteres de la contraseña.
     * @return contraseña generada.
     */
    private String generarPasswordAleatoria(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(caracteres.length());
            password.append(caracteres.charAt(index));
        }

        return password.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Usuario> findByNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Usuario> findByEstado(Integer estado) {
        return usuarioRepository.findByEnabled(estado);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario auth(String email, String password) {
        Usuario user = usuarioRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!user.getEnabled().equals(1)) {
            throw new BadCredentialsException("Usuario deshabilitado");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario register(RegisterRequestDTO dto) {
        return this.register(dto, "CLIENTE");
    }

    /**
     * Registra un nuevo usuario con un rol específico.
     * 
     * @param dto objeto con los datos del nuevo usuario.
     * @param rol rol a asignar (ej: "CLIENTE", "ADMIN", "EMPRESA").
     * @return entidad {@link Usuario} creada.
     */
    public Usuario register(RegisterRequestDTO dto, String rol) {
        if (usuarioRepository.existsById(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        Usuario user = Usuario.builder()
                .email(dto.getEmail())
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(1)
                .fechaRegistro(LocalDate.now())
                .rol(rol)
                .build();

        return usuarioRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        if (user.getEnabled() != 1) {
            throw new UsernameNotFoundException("Usuario deshabilitado");
        }

        return user;
    }

    /**
     * Registra un usuario con rol EMPRESA y devuelve la contraseña generada para su
     * acceso.
     * Este método es transaccional para asegurar consistencia entre usuario y
     * empresa.
     * 
     * @param dto datos de la empresa y usuario.
     * @return DTO con el usuario creado y la contraseña generada.
     */
    @Override
    @Transactional
    public UsuarioPasswordDTO registerEmpresa(EmpresaRegisterRequestDTO dto) {
        if (usuarioRepository.existsById(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        String rawPassword = generarPasswordAleatoria(10);

        Usuario user = Usuario.builder()
                .email(dto.getEmail())
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .password(passwordEncoder.encode(rawPassword))
                .enabled(1)
                .fechaRegistro(LocalDate.now())
                .rol("EMPRESA")
                .build();

        usuarioRepository.save(user);

        return UsuarioPasswordDTO.builder()
                .usuario(user)
                .passwordGenerada(rawPassword)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cambiarEstadoUsuario(String email, Integer nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEnabled(nuevoEstado);
        usuarioRepository.save(usuario);
    }
}
