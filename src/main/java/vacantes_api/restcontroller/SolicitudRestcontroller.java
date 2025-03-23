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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import vacantes_api.modelo.dto.SolicitudRequestDTO;
import vacantes_api.modelo.dto.SolicitudResponseDTO;
import vacantes_api.modelo.entity.Solicitud;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;
import vacantes_api.modelo.service.ISolicitudService;
import vacantes_api.modelo.service.IVacanteService;

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

    // Enpoints para la cliente

    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> create(@RequestBody @Valid SolicitudRequestDTO solicitudDTO) {

        // Obtenemos el usuario authenticado
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Buscamos la vacante que postula el usuario
        Vacante vacante = vacanteService.read(solicitudDTO.getIdVacante())
                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

        // Compruebo que el usuario no haya postulado a la vacante, aunque la base de
        // datos ya tiene un UNIQUE(id_Vacante,email). De esta forma evitamos una
        // excepcion que no podamos controlar bien
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

    @GetMapping("/mis-solicitudes")
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<List<SolicitudResponseDTO>> getMisSolicitudes() {

        // Obtenemos el usuario authenticado
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Solicitud> solicitudes = solicitudService.findByUsuarioEmail(usuario.getEmail());

        List<SolicitudResponseDTO> response = solicitudes.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudResponseDTO.class)).toList();

        return ResponseEntity.status(200).body(response);

    }

    // Podemos implementar un endpoint /{id} para ver detalle aunque la respuesta
    // sea igual que en el mis-solicitudes sin listar todas las solicitudes

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CLIENTE')")
    public ResponseEntity<Map<String, String>> cancelarSolicitud(@PathVariable Integer id) {

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Solicitud solicitud = solicitudService.read(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        if (!solicitud.getUsuario().getEmail().equals(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes cancelar esta solicitud");
        }

        // Hay que comprobar que la solicitud no haya sido adjudicada por la empresa
        if (solicitud.getEstado() == 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La solicitud ya fue adjudicada y no puede cancelarse");
        }

        solicitudService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Solicitud cancelada correctamente"));
    }

    // Enpoints para la empresa
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

        solicitud.setEstado(1);
        solicitudService.update(solicitud);

        return ResponseEntity.ok(Map.of(
                "message", "Solicitud adjudicada correctamente",
                "nombreCandidato", solicitud.getUsuario().getNombre(),
                "nombreVacante", solicitud.getVacante().getNombre()));

    }

    // Podria darse a la empresa la opcion de cancelar la adjudicacion por si se
    // equivoca con @PutMapping("/desadjudicar/{id}"). Lo dejamos para probar y poder quitar-poner la adjudicacion
    @PutMapping("/desadjudicar/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
    public ResponseEntity<Map<String, String>> desadjudicarSolicitud(@PathVariable Integer id) {
        Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Solicitud solicitud = solicitudService.read(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        if (!solicitud.getVacante().getEmpresa().getUsuario().getEmail().equals(empresaUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para modificar esta solicitud");
        }

        if (solicitud.getEstado() == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La solicitud ya no está adjudicada");
        }

        solicitud.setEstado(0);
        solicitudService.update(solicitud);

        return ResponseEntity.ok(Map.of(
                "message", "Adjudicación retirada correctamente",
                "nombreCandidato", solicitud.getUsuario().getNombre(),
                "nombreVacante", solicitud.getVacante().getNombre()));
    }

}
