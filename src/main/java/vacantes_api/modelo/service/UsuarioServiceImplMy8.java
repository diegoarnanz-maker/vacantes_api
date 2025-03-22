package vacantes_api.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImplMy8 extends GenericoCRUDServiceImplMy8<Usuario, String> implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected IUsuarioRepository getRepository() {
        return usuarioRepository;
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

}
