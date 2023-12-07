package pl.lderecki.dictservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lderecki.dictservice.DTO.DictDTO;
import pl.lderecki.dictservice.DTO.DictEntityDTO;
import pl.lderecki.dictservice.DTO.DictEntityUpdateDTO;
import pl.lderecki.dictservice.service.DictionaryService;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DictRestController {

    private final DictionaryService service;

    public DictRestController(DictionaryService service) {
        this.service = service;
    }

    @GetMapping("/dict_values")
    public ResponseEntity<Map<String, DictDTO>> getAll() {
        Map<String, DictDTO> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/dict_values/{dictId}/{dictKey}")
    public ResponseEntity<DictEntityDTO> getById(@PathVariable("dictId") String dictId,
                                                 @PathVariable("dictKey") String dictKey) {

        return ResponseEntity.ok(service.findEntityById(dictId, dictKey));
    }

    @PostMapping("/dict_values")
    public ResponseEntity<?> postDictEntity(@RequestBody DictEntityDTO entity) {
        DictEntityDTO saved = service.saveDictEntity(entity);

        return ResponseEntity.created(URI.create("/dict_values/" + saved.getDictId()
                                      + "/" + saved.getDictKey())).build();
    }

    @PutMapping("/dict_values/{dictId}/{dictKey}")
    public ResponseEntity<?> putDictEntity(@RequestBody DictEntityUpdateDTO entityDTO, @PathVariable("dictId") String dictId,
                                           @PathVariable("dictKey") String dictKey) {
        service.updateDictEntity(entityDTO, dictId, dictKey);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessageMapJson(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalStateException e) {
        return ResponseEntity.notFound().build();
    }

    private Map<String, String> errorMessageMapJson(String exceptionMessage) {
        Map<String, String> result = new HashMap<>();
        result.put("errorMessage", exceptionMessage);
        return result;
    }
}
