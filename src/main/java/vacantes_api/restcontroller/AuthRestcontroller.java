package vacantes_api.restcontroller;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vacantes_api.modelo.dto.LoginRequestDTO;
import vacantes_api.modelo.dto.LoginResponseDTO;
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

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }

    // @PostMapping("/register")
    // @GetMapping("/me")

}
