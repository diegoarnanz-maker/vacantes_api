package vacantes_api.restcontroller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import vacantes_api.modelo.dto.CategoriaRequestDTO;
import vacantes_api.modelo.dto.CategoriaResponseDTO;
import vacantes_api.modelo.entity.Categoria;
import vacantes_api.modelo.service.ICategoriaService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/categorias")
public class CategoriaRestcontroller {

    // Para el CRUD usaremos la siguiente convencion:
    // Un dto para la entrada de datos (RequestDto). El cliente no tiene por que
    // saber todos los campos de la entidad y menos si tienen objetos anidados.
    // Un dto para la salida de datos (ResponseDto). Evitando asi la exposicion de
    // datos innecesarios

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> findAll() {

        List<Categoria> categorias = categoriaService.findAll();
        List<CategoriaResponseDTO> response = categorias.stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaResponseDTO.class)).toList();

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<CategoriaResponseDTO>> findByNombre(@PathVariable String nombre) {
        List<Categoria> categorias = categoriaService.findByName(nombre);

        if (categorias.isEmpty()) {
            throw new RuntimeException("No se encontraron categorías con el nombre: " + nombre);
        }

        List<CategoriaResponseDTO> response = categorias.stream()
                .map(c -> modelMapper.map(c, CategoriaResponseDTO.class))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable Integer id) {
        Categoria categoria = categoriaService.read(id)
                .orElseThrow(() -> new RuntimeException("Categoria con id " + id + " no encontrada"));

        CategoriaResponseDTO response = modelMapper.map(categoria, CategoriaResponseDTO.class);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> create(@RequestBody @Valid CategoriaRequestDTO dto) {
        Categoria nuevaCategoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();

        Categoria guardada = categoriaService.create(nuevaCategoria);

        CategoriaResponseDTO response = modelMapper.map(guardada, CategoriaResponseDTO.class);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> update(@PathVariable Integer id,
            @RequestBody @Valid CategoriaRequestDTO dto) {

        Categoria categoria = categoriaService.read(id)
                .orElseThrow(() -> new RuntimeException("Categoria con id " + id + " no encontrada"));

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        Categoria actualizada = categoriaService.update(categoria);

        CategoriaResponseDTO response = modelMapper.map(actualizada, CategoriaResponseDTO.class);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {

        categoriaService.delete(id);

        return ResponseEntity.status(200).body(Map.of("message", "Categoría eliminada correctamente"));
    }

}
