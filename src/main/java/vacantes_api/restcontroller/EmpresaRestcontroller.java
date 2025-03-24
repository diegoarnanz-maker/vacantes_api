package vacantes_api.restcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerEmpresa(@RequestBody @Valid EmpresaRegisterRequestDTO dto) {

        UsuarioPasswordDTO datos = usuarioService.registerEmpresa(dto);

        Empresa empresa = empresaService.registerEmpresa(dto, datos.getUsuario());

        EmpresaResponseDTO response = modelMapper.map(empresa, EmpresaResponseDTO.class);

        // Mapear vacantes manualmente porque model mapper no puede mapear listas

        // Previene error si empresa.getVacantes() es null (porque no se ha
        // inicializado)
        response.setVacantes(
                empresa.getVacantes() != null
                        ? empresa.getVacantes().stream()
                                .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
                                .toList()
                        : List.of() // lista vacía para cuando se registre salga como [] y no null
        );

        return ResponseEntity.status(201).body(
                Map.of(
                        "empresa", response,
                        "passwordGenerada", datos.getPasswordGenerada()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMON')")
    public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresas() {
        List<EmpresaResponseDTO> response = empresaService.findAll().stream()
                .map(empresa -> {
                    EmpresaResponseDTO dto = modelMapper.map(empresa, EmpresaResponseDTO.class);
                    // Si hay vacantes, las mapeo; si no, devuelvo []
                    dto.setVacantes(
                            Optional.ofNullable(empresa.getVacantes())
                                    .orElse(List.of())
                                    .stream()
                                    .map(vacante -> modelMapper.map(vacante, VacanteResponseDTO.class))
                                    .toList());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(response);
    }

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

    //NO IMPLEMMENTADO EL DELETEEMPRESA, HAY QUE VER SI QUEREMOS QUE SI SE ELIMINE EMPRESA SE ELIMINE EL USUARIO O SE MARQUE SOLO COMO ENABLED = 0
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMON')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        empresaService.deleteEmpresa(id);
        return ResponseEntity.noContent().build();
    }

}
