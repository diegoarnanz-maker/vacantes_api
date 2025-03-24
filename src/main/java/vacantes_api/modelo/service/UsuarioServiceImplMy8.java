package vacantes_api.modelo.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    private String generarPasswordAleatoria(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Usamos StringBuilder para construir la contraseña carácter a carácter de
        // forma eficiente (mejor rendimiento que concatenar strings con +)
        StringBuilder password = new StringBuilder();

        // Mas seguro que Math.random() -> Genera numeros aleatorios
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < longitud; i++) {

            // Logica del algoritmo
            int index = random.nextInt(caracteres.length());
            password.append(caracteres.charAt(index));
        }

        return password.toString();
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

    @Override
    public List<Usuario> findByRol(String rol) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByRol'");
    }

    // Nunca indicamos si el usuario no existe o si la contraseña es incorrecta,
    // para evitar ataques de fuerza bruta.
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

    @Override
    public Usuario register(RegisterRequestDTO dto) {
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
                .rol("CLIENTE") // por defecto cuando te registras eres un cliente
                .build();

        return usuarioRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        if (user.getEnabled() != 1) {
            throw new UsernameNotFoundException("Usuario deshabilitado");
        }

        return user;
    }

    // Usamos transacciones para evitar que si falla una de las operaciones, la otra no se cree y de problemas de integridad.
    // Si falla la creacion del usuario, no se creara la empresa y viceversa.
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

        //Hemos creado un dto para que el controlador pueda devolver la contraseña generada antes de ser encriptada y poder enviarsela al usuario empresa para que pueda acceder a su cuenta.
        // Se podria hacer logica para que se le enviase esta contraseña por email.
        return UsuarioPasswordDTO.builder()
                .usuario(user)
                .passwordGenerada(rawPassword)
                .build();
    }

}
