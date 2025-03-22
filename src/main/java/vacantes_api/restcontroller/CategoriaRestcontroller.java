package vacantes_api.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/categorias")
public class CategoriaRestcontroller {

    // Para el CRUD usaremos la siguiente convencion:
    // Un dto para la entrada de datos (RequestDto). El cliente no tiene por que saber todos los campos de la entidad y menos si tienen objetos anidados.
    // Un dto para la salida de datos (ResponseDto). Evitando asi la exposicion de datos innecesarios

    // @GetMapping
    // public ResponseEntity<List<CategoriaResponseDto>> findAll() {

    //     return ResponseEntity.status(200).body(response);
    // }

    // @GetMapping("/{id}")
    // public ResponseEntity<CategoriaResponseDto> findById(@PathVariable Long id) {

    //     return ResponseEntity.status(200).body(response);
    // }

    // @PostMapping
    // public ResponseEntity<CategoriaResponseDto> create(@RequestBody @Valid CategoriaRequestDto dto) {

    //     return ResponseEntity.status(201).body(response);
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<CategoriaResponseDto> update(@PathVariable Long id,
    //         @RequestBody @Valid CategoriaRequestDto dto) {

    //     return ResponseEntity.status(200).body(response);
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> delete(@PathVariable Long id) {

    //     return ResponseEntity.status(204).build();
    // }

}
