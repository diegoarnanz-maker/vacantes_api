package vacantes_api.restcontroller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import vacantes_api.modelo.dto.SolicitudRequestDTO;
import vacantes_api.modelo.dto.SolicitudResponseDTO;
import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;
import vacantes_api.modelo.service.ISolicitudService;
import vacantes_api.modelo.service.IVacanteService;

/**
 * Controlador REST para la gestión de solicitudes de vacantes.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/solicitudes")
public class SolicitudRestcontroller {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ISolicitudService solicitudService;

    @Autowired
    private IVacanteService vacanteService;

    /**
     * Busca una solicitud por su ID.
     *
     * @param id Identificador de la solicitud.
     * @return Solicitud correspondiente en formato DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> findById(@PathVariable Integer id) {
        Solicitud solicitud = solicitudService.read(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        SolicitudResponseDTO response = modelMapper.map(solicitud, SolicitudResponseDTO.class);
        return ResponseEntity.status(200).body(response);
    }

    /**
     * Crea una nueva solicitud de vacante por parte de un cliente.
     *
     * @param solicitudDTO Datos de la solicitud.
     * @return Solicitud creada en formato DTO.
     */
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> create(@RequestBody @Valid SolicitudRequestDTO solicitudDTO) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Vacante vacante = vacanteService.read(solicitudDTO.getIdVacante())
                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

        Optional<Solicitud> existente = solicitudService.findByVacanteAndUsuario(vacante, usuario);
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una solicitud para esta vacante");
        }

        Solicitud solicitud = Solicitud.builder()
                .archivo(solicitudDTO.getArchivo())
                .curriculum(solicitudDTO.getCurriculum())
                .comentarios(solicitudDTO.getComentarios())
                .fecha(LocalDate.now())
                .estado(0)
                .vacante(vacante)
                .usuario(usuario)
                .build();

        Solicitud guardada = solicitudService.create(solicitud);
        SolicitudResponseDTO response = modelMapper.map(guardada, SolicitudResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene las solicitudes realizadas por el cliente autenticado.
     *
     * @return Lista de solicitudes propias del usuario.
     */
    @GetMapping("/mis-solicitudes")
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<List<SolicitudResponseDTO>> getMisSolicitudes() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Solicitud> solicitudes = solicitudService.findByUsuarioEmail(usuario.getEmail());

        List<SolicitudResponseDTO> response = solicitudes.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudResponseDTO.class)).toList();

        return ResponseEntity.status(200).body(response);
    }

    /**
     * Cancela una solicitud si aún no ha sido adjudicada y pertenece al cliente
     * autenticado.
     *
     * @param id ID de la solicitud.
     * @return Mensaje de cancelación.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<Map<String, String>> cancelarSolicitud(@PathVariable Integer id) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Solicitud solicitud = solicitudService.read(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        if (!solicitud.getUsuario().getEmail().equals(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes cancelar esta solicitud");
        }

        if (solicitud.getEstado() == 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La solicitud ya fue adjudicada y no puede cancelarse");
        }

        solicitudService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Solicitud cancelada correctamente"));
    }

    /**
     * Obtiene todas las solicitudes de una vacante, accesible por la empresa
     * propietaria.
     *
     * @param idVacante ID de la vacante.
     * @return Lista de solicitudes.
     */
    @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
    @GetMapping("/vacante/{idVacante}")
    public ResponseEntity<List<SolicitudResponseDTO>> getSolicitudesPorVacante(@PathVariable Integer idVacante) {
        Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Vacante vacante = vacanteService.read(idVacante)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacante no encontrada"));

        if (!vacante.getEmpresa().getUsuario().getEmail().equals(empresaUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para ver las solicitudes de esta vacante");
        }

        List<Solicitud> solicitudes = solicitudService.findByVacante(vacante);

        List<SolicitudResponseDTO> response = solicitudes.stream()
                .map(s -> modelMapper.map(s, SolicitudResponseDTO.class))
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Adjudica una solicitud seleccionada por la empresa propietaria de la vacante.
     *
     * @param id ID de la solicitud.
     * @return Mensaje de adjudicación con datos del candidato y la vacante.
     */
    @PutMapping("/adjudicar/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
    public ResponseEntity<Map<String, String>> adjudicarSolicitud(@PathVariable Integer id) {
        Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Solicitud solicitud = solicitudService.read(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        if (!solicitud.getVacante().getEmpresa().getUsuario().getEmail().equals(empresaUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para adjudicar esta solicitud");
        }

        if (solicitud.getEstado() == 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La solicitud ya fue adjudicada anteriormente");
        }

        solicitudService.adjudicarSolicitud(id);

        return ResponseEntity.ok(Map.of(
                "message", "Solicitud adjudicada correctamente",
                "nombreCandidato", solicitud.getUsuario().getNombre(),
                "nombreVacante", solicitud.getVacante().getNombre()));
    }

    /**
     * Rechaza una solicitud que ha sido adjudicada previamente.
     *
     * @param id ID de la solicitud.
     * @return Mensaje de rechazo con datos del candidato y la vacante.
     */
    @PutMapping("/rechazar/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
    public ResponseEntity<Map<String, String>> rechazarSolicitud(@PathVariable Integer id) {
        Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Solicitud solicitud = solicitudService.read(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        if (!solicitud.getVacante().getEmpresa().getUsuario().getEmail().equals(empresaUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para modificar esta solicitud");
        }

        if (solicitud.getEstado() == 2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La solicitud ya no está adjudicada");
        }

        solicitudService.rechazarSolicitud(id);

        return ResponseEntity.ok(Map.of(
                "message", "Solicitud rechazada correctamente",
                "nombreCandidato", solicitud.getUsuario().getNombre(),
                "nombreVacante", solicitud.getVacante().getNombre()));
    }
}
