package vacantes_api.restcontroller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vacantes_api.modelo.dto.UsuarioPerfilUpdateDTO;
import vacantes_api.modelo.dto.UsuarioRequestDTO;
import vacantes_api.modelo.dto.UsuarioResponseDTO;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.service.IUsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuarios")
public class UsuarioRestcontroller {

        @Autowired
        private IUsuarioService usuarioService;

        @Autowired
        private ModelMapper modelMapper;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @GetMapping
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<UsuarioResponseDTO>> getUsuarios() {
                List<Usuario> usuarios = usuarioService.findAll();
                List<UsuarioResponseDTO> response = usuarios.stream()
                                .map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class))
                                .toList();
                return ResponseEntity.status(200).body(response);
        }

        @GetMapping("/{email}")
        public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable String email) {
                Usuario usuario = usuarioService.read(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                UsuarioResponseDTO response = modelMapper.map(usuario, UsuarioResponseDTO.class);
                return ResponseEntity.status(200).body(response);
        }

        @GetMapping("/buscar/nombre/{nombre}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
                List<UsuarioResponseDTO> usuarios = usuarioService.findByNombre(nombre).stream()
                                .map(u -> modelMapper.map(u, UsuarioResponseDTO.class))
                                .toList();
                return ResponseEntity.ok(usuarios);
        }

        @GetMapping("/buscar/rol/{rol}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<UsuarioResponseDTO>> buscarPorRol(@PathVariable String rol) {
                List<UsuarioResponseDTO> usuarios = usuarioService.findByRol(rol).stream()
                                .map(u -> modelMapper.map(u, UsuarioResponseDTO.class))
                                .toList();
                return ResponseEntity.ok(usuarios);
        }

        @GetMapping("/buscar/estado/{estado}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<UsuarioResponseDTO>> buscarPorEstado(@PathVariable Integer estado) {
                List<UsuarioResponseDTO> usuarios = usuarioService.findByEstado(estado).stream()
                                .map(u -> modelMapper.map(u, UsuarioResponseDTO.class))
                                .toList();
                return ResponseEntity.ok(usuarios);
        }

        // Update mas completo para cambiar rol/estado/password...
        @PutMapping("/{email}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<UsuarioResponseDTO> updateUsuario(
                        @PathVariable String email,
                        @RequestBody @Valid UsuarioRequestDTO dto) {

                Usuario original = usuarioService.read(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Usuario.UsuarioBuilder builder = original.toBuilder()
                                .nombre(dto.getNombre())
                                .apellidos(dto.getApellidos())
                                .enabled(dto.getEnabled())
                                .rol(dto.getRol());

                if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                        builder.password(passwordEncoder.encode(dto.getPassword()));
                }

                Usuario actualizado = builder.build();

                usuarioService.update(actualizado);

                UsuarioResponseDTO response = modelMapper.map(actualizado, UsuarioResponseDTO.class);

                return ResponseEntity.ok(response);
        }

        // Updates directos para activar/desactivar usuario
        @PutMapping("/desactivar/{email}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<Map<String, String>> desactivarUsuario(@PathVariable String email) {
                usuarioService.cambiarEstadoUsuario(email, 0);
                return ResponseEntity.ok(
                                Map.of(
                                                "message", "Usuario desactivado por mal uso",
                                                "email", email));
        }

        @PutMapping("/activar/{email}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<Map<String, String>> activarUsuario(@PathVariable String email) {
                usuarioService.cambiarEstadoUsuario(email, 1);
                return ResponseEntity.ok(
                                Map.of(
                                                "message", "Usuario activado correctamente",
                                                "email", email));
        }

        @PutMapping("/perfil")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<UsuarioResponseDTO> actualizarPerfilUsuario(
                        @RequestBody @Valid UsuarioPerfilUpdateDTO dto,
                        Authentication authentication) {

                String email = authentication.getName();

                Usuario usuario = usuarioService.read(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                usuario.setNombre(dto.getNombre());
                usuario.setApellidos(dto.getApellidos());

                usuarioService.update(usuario);

                UsuarioResponseDTO response = modelMapper.map(usuario, UsuarioResponseDTO.class);
                return ResponseEntity.ok(response);
        }

}
