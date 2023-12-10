package pl.lderecki.crudservice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lderecki.crudservice.DTO.SimpleEntityReadDTO;
import pl.lderecki.crudservice.DTO.SimpleEntityWriteDTO;
import pl.lderecki.crudservice.service.SimpleEntityService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simple_entity")
public class SimpleController {

    private final SimpleEntityService service;

    public SimpleController(SimpleEntityService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SimpleEntityReadDTO>> get() {
        List<SimpleEntityReadDTO> result = service.findAll();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleEntityReadDTO> get(@PathVariable("id") long id) {
        SimpleEntityReadDTO result = service.findById(id);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<SimpleEntityReadDTO> post(@RequestBody SimpleEntityWriteDTO toWriteDTO) {
        toWriteDTO.setId(null);

        SimpleEntityReadDTO result = service.save(toWriteDTO);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable("id") long id, @RequestBody SimpleEntityWriteDTO toUpdateDTO) {
        toUpdateDTO.setId(id);
        service.update(toUpdateDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }
}
