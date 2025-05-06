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
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vacantes_api.modelo.dto.EmpresaPerfilUpdateDTO;
import vacantes_api.modelo.dto.UsuarioPerfilUpdateDTO;
import vacantes_api.modelo.dto.UsuarioRequestDTO;
import vacantes_api.modelo.dto.UsuarioResponseDTO;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.service.IUsuarioService;

/**
 * Controlador REST para la gestión de usuarios.
 */
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

        /**
         * Obtiene todos los usuarios del sistema. Solo accesible para administradores.
         *
         * @return Lista de usuarios.
         */
        @GetMapping
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<UsuarioResponseDTO>> getUsuarios() {
                List<Usuario> usuarios = usuarioService.findAll();
                List<UsuarioResponseDTO> response = usuarios.stream()
                                .map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class))
                                .toList();
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Obtiene un usuario por su email.
         *
         * @param email Email del usuario.
         * @return Usuario correspondiente en formato DTO.
         */
        @GetMapping("/{email}")
        public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable String email) {
                Usuario usuario = usuarioService.read(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                UsuarioResponseDTO response = modelMapper.map(usuario, UsuarioResponseDTO.class);
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Busca usuarios por nombre. Solo accesible para administradores.
         *
         * @param nombre Nombre del usuario.
         * @return Lista de usuarios encontrados.
         */
        @GetMapping("/buscar/nombre/{nombre}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
                List<UsuarioResponseDTO> usuarios = usuarioService.findByNombre(nombre).stream()
                                .map(u -> modelMapper.map(u, UsuarioResponseDTO.class))
                                .toList();
                return ResponseEntity.ok(usuarios);
        }

        /**
         * Busca usuarios por rol. Solo accesible para administradores.
         *
         * @param rol Rol del usuario.
         * @return Lista de usuarios con el rol especificado.
         */
        @GetMapping("/buscar/rol/{rol}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<UsuarioResponseDTO>> buscarPorRol(@PathVariable String rol) {
                List<UsuarioResponseDTO> usuarios = usuarioService.findByRol(rol).stream()
                                .map(u -> modelMapper.map(u, UsuarioResponseDTO.class))
                                .toList();
                return ResponseEntity.ok(usuarios);
        }

        /**
         * Busca usuarios por su estado (activo/inactivo). Solo accesible para
         * administradores.
         *
         * @param estado Estado del usuario (1 activo, 0 inactivo).
         * @return Lista de usuarios con el estado especificado.
         */
        @GetMapping("/buscar/estado/{estado}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<UsuarioResponseDTO>> buscarPorEstado(@PathVariable Integer estado) {
                List<UsuarioResponseDTO> usuarios = usuarioService.findByEstado(estado).stream()
                                .map(u -> modelMapper.map(u, UsuarioResponseDTO.class))
                                .toList();
                return ResponseEntity.ok(usuarios);
        }

        /**
         * Actualiza completamente los datos de un usuario, incluyendo contraseña, rol y
         * estado.
         * Solo accesible para administradores.
         *
         * @param email Email del usuario a actualizar.
         * @param dto   Datos nuevos del usuario.
         * @return Usuario actualizado.
         */
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

        /**
         * Desactiva un usuario por su email. Solo accesible para administradores.
         *
         * @param email Email del usuario.
         * @return Mensaje de confirmación.
         */
        @PutMapping("/desactivar/{email}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<Map<String, String>> desactivarUsuario(@PathVariable String email) {
                usuarioService.cambiarEstadoUsuario(email, 0);
                return ResponseEntity.ok(
                                Map.of("message", "Usuario desactivado por mal uso", "email", email));
        }

        /**
         * Activa un usuario por su email. Solo accesible para administradores.
         *
         * @param email Email del usuario.
         * @return Mensaje de confirmación.
         */
        @PutMapping("/activar/{email}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<Map<String, String>> activarUsuario(@PathVariable String email) {
                usuarioService.cambiarEstadoUsuario(email, 1);
                return ResponseEntity.ok(
                                Map.of("message", "Usuario activado correctamente", "email", email));
        }

        /**
         * Permite a un usuario actualizar su propio perfil (nombre y apellidos).
         *
         * @param dto            Datos del perfil.
         * @param authentication Objeto de autenticación con el email del usuario.
         * @return Perfil actualizado.
         */
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

        /**
         * Permite a una empresa actualizar su perfil asociado.
         *
         * @param dto            Datos del perfil de la empresa.
         * @param authentication Objeto de autenticación con el email del usuario.
         * @return Mensaje de confirmación.
         */
        @PutMapping("/perfil/empresa")
        @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
        public ResponseEntity<Map<String, String>> actualizarPerfilEmpresa(
                        @RequestBody @Valid EmpresaPerfilUpdateDTO dto,
                        Authentication authentication) {

                String email = authentication.getName();

                Usuario usuario = usuarioService.read(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                if (usuario.getEmpresa() == null) {
                        throw new RuntimeException("No se encontró empresa asociada al usuario.");
                }

                usuario.getEmpresa().setNombreEmpresa(dto.getNombreEmpresa());
                usuario.getEmpresa().setCif(dto.getCif());
                usuario.getEmpresa().setDireccionFiscal(dto.getDireccionFiscal());
                usuario.getEmpresa().setPais(dto.getPais());

                usuarioService.update(usuario);

                return ResponseEntity.ok(Map.of("message", "Perfil de empresa actualizado correctamente"));
        }
}
