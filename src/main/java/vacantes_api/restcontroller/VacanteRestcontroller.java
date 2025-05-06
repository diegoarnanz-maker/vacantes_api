package vacantes_api.restcontroller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import vacantes_api.modelo.dto.VacanteRequestDTO;
import vacantes_api.modelo.dto.VacanteResponseDTO;
import vacantes_api.modelo.entity.Categoria;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;
import vacantes_api.modelo.service.ICategoriaService;
import vacantes_api.modelo.service.IEmpresaService;
import vacantes_api.modelo.service.IVacanteService;

/**
 * Controlador REST para la gestión de vacantes.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/vacantes")
public class VacanteRestcontroller {

        @Autowired
        private ModelMapper modelMapper;

        @Autowired
        private IVacanteService vacanteService;

        @Autowired
        private IEmpresaService empresaService;

        @Autowired
        private ICategoriaService categoriaService;

        /**
         * Obtiene todas las vacantes registradas.
         *
         * @return Lista de vacantes.
         */
        @GetMapping
        public ResponseEntity<List<VacanteResponseDTO>> findAll() {
                List<Vacante> vacantes = vacanteService.findAll();
                List<VacanteResponseDTO> response = vacantes.stream()
                                .map(vacante -> modelMapper.map(vacante, VacanteResponseDTO.class))
                                .toList();
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Obtiene una vacante por su ID.
         *
         * @param id Identificador de la vacante.
         * @return Vacante encontrada.
         */
        @GetMapping("/{id}")
        public ResponseEntity<VacanteResponseDTO> findById(@PathVariable Integer id) {
                Vacante vacante = vacanteService.read(id)
                                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));
                VacanteResponseDTO response = modelMapper.map(vacante, VacanteResponseDTO.class);
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Busca vacantes por su nombre.
         *
         * @param nombre Nombre de la vacante.
         * @return Lista de vacantes coincidentes.
         */
        @GetMapping("/buscar/{nombre}")
        public ResponseEntity<List<VacanteResponseDTO>> findByNombre(@PathVariable String nombre) {
                List<Vacante> vacantes = vacanteService.findByNombre(nombre);
                List<VacanteResponseDTO> response = vacantes.stream()
                                .map(vacante -> modelMapper.map(vacante, VacanteResponseDTO.class))
                                .toList();
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Busca vacantes por nombre de categoría.
         *
         * @param nombre Nombre de la categoría.
         * @return Lista de vacantes asociadas a esa categoría.
         */
        @GetMapping("/categoria/{nombre}")
        public ResponseEntity<List<VacanteResponseDTO>> findByCategoriaNombre(@PathVariable String nombre) {
                List<Vacante> vacantes = vacanteService.findByCategoriaNombre(nombre);
                List<VacanteResponseDTO> response = vacantes.stream()
                                .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
                                .toList();
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Busca vacantes por nombre de empresa.
         *
         * @param nombreEmpresa Nombre de la empresa.
         * @return Lista de vacantes publicadas por esa empresa.
         */
        @GetMapping("/empresa/{nombreEmpresa}")
        public ResponseEntity<List<VacanteResponseDTO>> findByEmpresaNombre(@PathVariable String nombreEmpresa) {
                List<Vacante> vacantes = vacanteService.findByEmpresaNombreEmpresa(nombreEmpresa);
                List<VacanteResponseDTO> response = vacantes.stream()
                                .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
                                .toList();
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Busca vacantes por salario ofrecido.
         *
         * @param salario Valor del salario.
         * @return Lista de vacantes que ofrecen ese salario.
         */
        @GetMapping("/salario/{salario}")
        public ResponseEntity<List<VacanteResponseDTO>> findBySalario(@PathVariable Double salario) {
                List<Vacante> vacantes = vacanteService.findBySalario(salario);
                List<VacanteResponseDTO> dto = vacantes.stream()
                                .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
                                .toList();
                return ResponseEntity.ok(dto);
        }

        /**
         * Obtiene las vacantes publicadas por la empresa logueada.
         *
         * @return Lista de vacantes propias.
         */
        @GetMapping("/propias")
        public ResponseEntity<List<VacanteResponseDTO>> findVacantesPropias() {
                Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Empresa empresa = empresaService.findByUsuarioEmail(empresaUser.getEmail())
                                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
                List<Vacante> vacantesPropias = empresa.getVacantes();
                List<VacanteResponseDTO> response = vacantesPropias.stream()
                                .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
                                .toList();
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Crea una nueva vacante para la empresa logueada.
         *
         * @param dto Datos de la vacante.
         * @return Vacante creada.
         */
        @PostMapping
        @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
        public ResponseEntity<VacanteResponseDTO> create(@RequestBody @Valid VacanteRequestDTO dto) {
                Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Empresa empresa = empresaService.findByUsuarioEmail(empresaUser.getEmail())
                                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
                Categoria categoria = categoriaService.read(dto.getIdCategoria())
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

                Vacante vacante = Vacante.builder()
                                .nombre(dto.getNombre())
                                .descripcion(dto.getDescripcion())
                                .fecha(dto.getFecha())
                                .salario(dto.getSalario())
                                .estatus(Vacante.Estatus.CREADA)
                                .destacado(dto.getDestacado())
                                .imagen(dto.getImagen())
                                .detalles(dto.getDetalles())
                                .categoria(categoria)
                                .empresa(empresa)
                                .build();

                Vacante guardada = vacanteService.create(vacante);
                VacanteResponseDTO response = modelMapper.map(guardada, VacanteResponseDTO.class);
                return ResponseEntity.status(201).body(response);
        }

        /**
         * Actualiza una vacante existente publicada por la empresa logueada.
         *
         * @param id  ID de la vacante.
         * @param dto Nuevos datos de la vacante.
         * @return Vacante actualizada.
         */
        @PutMapping("/{id}")
        @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
        public ResponseEntity<VacanteResponseDTO> update(@PathVariable Integer id,
                        @RequestBody @Valid VacanteRequestDTO dto) {
                Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Empresa empresa = empresaService.findByUsuarioEmail(empresaUser.getEmail())
                                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

                Vacante vacante = vacanteService.read(id)
                                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

                if (!vacante.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "No tienes permisos para modificar esta vacante");
                }

                Categoria categoria = categoriaService.read(dto.getIdCategoria())
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

                Vacante actualizada = vacante.toBuilder()
                                .nombre(dto.getNombre())
                                .descripcion(dto.getDescripcion())
                                .fecha(dto.getFecha())
                                .salario(dto.getSalario())
                                .estatus(dto.getEstatus())
                                .destacado(dto.getDestacado())
                                .imagen(dto.getImagen())
                                .detalles(dto.getDetalles())
                                .categoria(categoria)
                                .build();

                Vacante guardada = vacanteService.update(actualizada);
                VacanteResponseDTO response = modelMapper.map(guardada, VacanteResponseDTO.class);
                return ResponseEntity.status(200).body(response);
        }

        /**
         * Elimina una vacante publicada por la empresa logueada (cambio de estado a
         * CANCELADA).
         *
         * @param id ID de la vacante.
         * @return Mensaje de confirmación.
         */
        @DeleteMapping("/{id}")
        @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
        public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
                Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Empresa empresa = empresaService.findByUsuarioEmail(empresaUser.getEmail())
                                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
                Vacante vacante = vacanteService.read(id)
                                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

                if (!vacante.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "No tienes permisos para eliminar esta vacante");
                }

                vacanteService.delete(id);
                return ResponseEntity.status(200).body(Map.of("message", "Vacante eliminada correctamente"));
        }
}
