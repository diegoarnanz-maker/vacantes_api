package vacantes_api.restcontroller;

import java.util.List;
import java.util.Map;

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
import vacantes_api.modelo.dto.VacanteRequestDTO;
import vacantes_api.modelo.dto.VacanteResponseDTO;
import vacantes_api.modelo.entity.Categoria;
import vacantes_api.modelo.entity.Empresa;
import vacantes_api.modelo.entity.Usuario;
import vacantes_api.modelo.entity.Vacante;
import vacantes_api.modelo.service.ICategoriaService;
import vacantes_api.modelo.service.IEmpresaService;
import vacantes_api.modelo.service.IVacanteService;

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

    @GetMapping
    public ResponseEntity<List<VacanteResponseDTO>> findAll() {

        List<Vacante> vacantes = vacanteService.findAll();

        List<VacanteResponseDTO> response = vacantes.stream()
                .map(vacante -> modelMapper.map(vacante, VacanteResponseDTO.class))
                .toList();

        return ResponseEntity.status(200).body(response);
    }
    
   
     @GetMapping("/{id}")
    public ResponseEntity<VacanteResponseDTO> findById(@PathVariable Integer id) {

        Vacante vacante = vacanteService.read(id)
                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

        VacanteResponseDTO response = modelMapper.map(vacante, VacanteResponseDTO.class);

        return ResponseEntity.status(200).body(response);
    }

    //Los usuarios pueden buscar vacantes de empleo utilizando filtros como empresas, tipo de contrato, categoría. Solo verán aquellas vacantes que están en estado “CREADA”.

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<VacanteResponseDTO>> findByNombre(@PathVariable String nombre) {

        List<Vacante> vacantes = vacanteService.findByNombre(nombre);

        List<VacanteResponseDTO> response = vacantes.stream()
                .map(vacante -> modelMapper.map(vacante, VacanteResponseDTO.class))
                .toList();

        return ResponseEntity.status(200).body(response);
    }
    
    /* NO hace falta
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<VacanteResponseDTO>> findByCategoria(@PathVariable Integer idCategoria) {
        List<Vacante> vacantes = vacanteService.findByCategoriaId(idCategoria);

        List<VacanteResponseDTO> response = vacantes.stream()
                .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
                .toList();

        return ResponseEntity.status(200).body(response);
    }
    */
    
   //Se añaden los 2 métodos que faltaban para las búsquedas: 
    
    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<VacanteResponseDTO>> findByCategoriaNombre(@PathVariable String nombre){
    	
    	List<Vacante> vacantes = vacanteService.findByCategoriaNombre(nombre);
    	
    	List<VacanteResponseDTO> response = vacantes.stream()
    			.map(v-> modelMapper.map(v, VacanteResponseDTO.class))
    			.toList();
    	return ResponseEntity.status(200).body(response);
    	
    }
    
   @GetMapping("/empresa/{nombreEmpresa}") 
   public ResponseEntity<List<VacanteResponseDTO>> findByEmpresaNombre(@PathVariable String nombreEmpresa){
	   List<Vacante> vacantes = vacanteService.findByEmpresaNombreEmpresa(nombreEmpresa);
	   
	   List<VacanteResponseDTO> response = vacantes.stream()
			   .map(v-> modelMapper.map(v, VacanteResponseDTO.class))
			   .toList();
	   return ResponseEntity.status(200).body(response);
   }
   
   
   //Método buscar por salario:
   
   
   @GetMapping("/salario/{salario}")
   public ResponseEntity<List<VacanteResponseDTO>> findBySalario(
           @PathVariable Double salario) {

       List<Vacante> vacantes = vacanteService.findBySalario(salario);
       List<VacanteResponseDTO> dto = vacantes.stream()
           .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
           .toList();

       return ResponseEntity.ok(dto);
   }
   
   //Método añadido para buscar vacante por estado: 		VALORAR SI ES NECESARIO
   
   /*@GetMapping("/estado/{estado}")
   public ResponseEntity<List<VacanteResponseDTO>> findByEstado(
           @PathVariable String estado) {
       List<Vacante> vacantes = vacanteService.findByEstado(estado);
       List<VacanteResponseDTO> dto = vacantes.stream()
           .map(v -> modelMapper.map(v, VacanteResponseDTO.class))
           .toList();
       return ResponseEntity.ok(dto);
   }*/
   
    
   //Se añade el método para buscar todas las vacantes que pertenecen a la EMPRESA LOGEADA:
   
   @GetMapping("/propias")
   public ResponseEntity<List<VacanteResponseDTO>> findVacantesPropias(){
	   // Obtener el usuario autenticado
       Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       // Obtener la empresa asociada a ese usuario
       Empresa empresa = empresaService.findByUsuarioEmail(empresaUser.getEmail())
               .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
	   
       List<Vacante> vacantesPropias = empresa.getVacantes();//Se adquiere la lista de vacantes asociadas a esa empresa
       
       List<VacanteResponseDTO> response = vacantesPropias.stream()
    		   .map(v-> modelMapper.map(v, VacanteResponseDTO.class))
    		   .toList();
       return ResponseEntity.status(200).body(response);
   }
   

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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
    public ResponseEntity<VacanteResponseDTO> update(@PathVariable Integer id,
            @RequestBody @Valid VacanteRequestDTO dto) {

        // Obtener el usuario autenticado
        Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Obtener la empresa asociada a ese usuario
        Empresa empresa = empresaService.findByUsuarioEmail(empresaUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        // Buscar la vacante a editar
        Vacante vacante = vacanteService.read(id)
                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

        // Verificar que la vacante pertenezca a la empresa logueada
        if (!vacante.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para modificar esta vacante");
        }

        // Validar la categoría
        Categoria categoria = categoriaService.read(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Actualizar datos
        Vacante actualizada = vacante.toBuilder() // Para put hay que usar toBuilder y NO builder como en post
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .fecha(dto.getFecha())
                .salario(dto.getSalario())
                .estatus(dto.getEstatus()) // Aqui damos opcion a cambiar el estatus
                .destacado(dto.getDestacado())
                .imagen(dto.getImagen())
                .detalles(dto.getDetalles())
                .categoria(categoria)
                .build();

        Vacante guardada = vacanteService.update(actualizada); //UPDATE sobreescrito en ServiceIMpl

        VacanteResponseDTO response = modelMapper.map(guardada, VacanteResponseDTO.class);

        return ResponseEntity.status(200).body(response);
    }

    //NO SE ELIMINAN SINO QUE SE CAMBIA EL ESTATUS A CANCELADA (método delete sobreescrito)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        Usuario empresaUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Empresa empresa = empresaService.findByUsuarioEmail(empresaUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        Vacante vacante = vacanteService.read(id)
                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

        if (!vacante.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para eliminar esta vacante");
        }
     

        vacanteService.delete(id);

        return ResponseEntity.status(200).body(Map.of("message", "Vacante eliminada correctamente"));
    }
    
    
    

}
