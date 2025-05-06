package vacantes_api.restcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vacantes_api.modelo.dto.EmpresaRegisterRequestDTO;
import vacantes_api.modelo.dto.EmpresaResponseDTO;
import vacantes_api.modelo.dto.UsuarioPasswordDTO;
import vacantes_api.modelo.dto.VacanteResponseDTO;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.service.IEmpresaService;
import vacantes_api.modelo.service.IUsuarioService;

/**
 * Controlador REST para la gestión de empresas y su registro.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/empresas")
public class EmpresaRestcontroller {

        @Autowired
        private IEmpresaService empresaService;

        @Autowired
        private IUsuarioService usuarioService;

        @Autowired
        private ModelMapper modelMapper;

        /**
         * Registra una nueva empresa junto a su usuario asociado.
         *
         * @param dto Datos para el registro de la empresa y su usuario.
         * @return Empresa registrada junto con la contraseña generada.
         */
        @PostMapping("/register")
        public ResponseEntity<Map<String, Object>> registerEmpresa(@RequestBody @Valid EmpresaRegisterRequestDTO dto) {
                UsuarioPasswordDTO datos = usuarioService.registerEmpresa(dto);
                Empresa empresa = empresaService.registerEmpresa(dto, datos.getUsuario());

                EmpresaResponseDTO response = modelMapper.map(empresa, EmpresaResponseDTO.class);
                response.setVacantes(
                                empresa.getVacantes() != null
                                                ? empresa.getVacantes().stream()
                                                                .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
                                                                .toList()
                                                : List.of());

                return ResponseEntity.status(201).body(
                                Map.of(
                                                "empresa", response,
                                                "passwordGenerada", datos.getPasswordGenerada()));
        }

        /**
         * Lista todas las empresas activas con sus vacantes.
         *
         * @return Lista de empresas activas.
         */
        @GetMapping
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresas() {
                List<EmpresaResponseDTO> response = empresaService.findAll().stream()
                                .filter(emp -> emp.getUsuario() != null && emp.getUsuario().getEnabled() == 1)
                                .map(empresa -> {
                                        EmpresaResponseDTO dto = modelMapper.map(empresa, EmpresaResponseDTO.class);
                                        dto.setVacantes(Optional.ofNullable(empresa.getVacantes())
                                                        .orElse(List.of())
                                                        .stream()
                                                        .map(vac -> modelMapper.map(vac, VacanteResponseDTO.class))
                                                        .toList());
                                        return dto;
                                })
                                .toList();

                return ResponseEntity.ok(response);
        }

        /**
         * Lista todas las empresas desactivadas.
         *
         * @return Lista de empresas desactivadas.
         */
        @GetMapping("/desactivadas")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresasDesactivadas() {
                List<EmpresaResponseDTO> response = empresaService.findAll().stream()
                                .filter(emp -> emp.getUsuario() != null && emp.getUsuario().getEnabled() == 0)
                                .map(empresa -> {
                                        EmpresaResponseDTO dto = modelMapper.map(empresa, EmpresaResponseDTO.class);
                                        dto.setVacantes(Optional.ofNullable(empresa.getVacantes())
                                                        .orElse(List.of())
                                                        .stream()
                                                        .map(vac -> modelMapper.map(vac, VacanteResponseDTO.class))
                                                        .toList());
                                        return dto;
                                })
                                .toList();

                return ResponseEntity.ok(response);
        }

        /**
         * Busca una empresa por su ID.
         *
         * @param id Identificador de la empresa.
         * @return Empresa encontrada con sus vacantes.
         */
        @GetMapping("/{id}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<EmpresaResponseDTO> findById(@PathVariable Integer id) {
                Empresa empresa = empresaService.read(id)
                                .orElseThrow(() -> new RuntimeException("Empresa con id " + id + " no encontrada"));

                EmpresaResponseDTO response = modelMapper.map(empresa, EmpresaResponseDTO.class);
                response.setVacantes(
                                Optional.ofNullable(empresa.getVacantes())
                                                .orElse(List.of())
                                                .stream()
                                                .map(vacante -> modelMapper.map(vacante, VacanteResponseDTO.class))
                                                .toList());

                return ResponseEntity.ok(response);
        }

        /**
         * Actualiza los datos de una empresa.
         *
         * @param id  ID de la empresa a actualizar.
         * @param dto Nuevos datos de la empresa.
         * @return Empresa actualizada.
         */
        @PutMapping("/{id}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<EmpresaResponseDTO> update(@PathVariable Integer id,
                        @RequestBody @Valid EmpresaRegisterRequestDTO dto) {
                Empresa empresa = empresaService.updateEmpresa(id, dto);

                EmpresaResponseDTO response = modelMapper.map(empresa, EmpresaResponseDTO.class);
                response.setVacantes(
                                Optional.ofNullable(empresa.getVacantes())
                                                .orElse(List.of())
                                                .stream()
                                                .map(vacante -> modelMapper.map(vacante, VacanteResponseDTO.class))
                                                .toList());

                return ResponseEntity.ok(response);
        }

        /**
         * Desactiva una empresa, marcando como inactivo al usuario vinculado.
         *
         * @param id ID de la empresa.
         * @return Mensaje de confirmación.
         */
        @PutMapping("/desactivar/{id}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<Map<String, String>> desactivarEmpresa(@PathVariable Integer id) {
                empresaService.setEstadoUsuarioEmpresa(id, 0);
                return ResponseEntity.ok(Map.of("message", "Empresa desactivada correctamente"));
        }

        /**
         * Activa una empresa, reactivando al usuario vinculado.
         *
         * @param id ID de la empresa.
         * @return Mensaje de confirmación.
         */
        @PutMapping("/activar/{id}")
        @PreAuthorize("hasAuthority('ROLE_ADMON')")
        public ResponseEntity<Map<String, String>> activarEmpresa(@PathVariable Integer id) {
                empresaService.setEstadoUsuarioEmpresa(id, 1);
                return ResponseEntity.ok(Map.of("message", "Empresa activada correctamente"));
        }
}
