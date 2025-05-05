package vacantes_api.restcontroller;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vacantes_api.modelo.dto.LoginRequestDTO;
import vacantes_api.modelo.dto.LoginResponseDTO;
import vacantes_api.modelo.dto.RegisterRequestDTO;
import vacantes_api.modelo.dto.UsuarioResponseDTO;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.service.IUsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthRestcontroller {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDto) {
        Usuario user = usuarioService.auth(loginDto.getEmail(), loginDto.getPassword());

        var authToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        LoginResponseDTO response = modelMapper.map(user, LoginResponseDTO.class);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto) {
        Usuario user = usuarioService.register(dto);

        // Opcional, muchas apps lo utilizan para mejorar UX
        var authToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        //

        LoginResponseDTO response = modelMapper.map(user, LoginResponseDTO.class);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/me1")
    public ResponseEntity<UsuarioResponseDTO> me() {
        Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsuarioResponseDTO dto = modelMapper.map(user, UsuarioResponseDTO.class);
        return ResponseEntity.status(200).body(dto);
    }

    // CAMBIO SUGERIDO DEL MÉTODO ME (así si modificamos los datos del usuario
    // authenticado cuando clique a Mi Perfil, estos saldrán actualizados.:

    @GetMapping("/me2")
    public ResponseEntity<UsuarioResponseDTO> me2(Authentication authentication) {
        String email = authentication.getName();
        Usuario user = usuarioService.findByEmail(email);

        UsuarioResponseDTO userDto = modelMapper.map(user, UsuarioResponseDTO.class);

        return ResponseEntity.status(200).body(userDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(Authentication authentication) {
        String email = authentication.getName();
        Usuario user = usuarioService.findByEmail(email);
        UsuarioResponseDTO userDto = modelMapper.map(user, UsuarioResponseDTO.class);
        return ResponseEntity.ok(userDto);
    }

}
