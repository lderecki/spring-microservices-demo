package pl.lderecki.aiapiconsumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lderecki.aiapiconsumer.service.ImageService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getImageByDescription(@RequestParam String query) {
        byte[] response = service.postForImage(query);
        log.info("Image generation done");
        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessageMapJson(e.getMessage()));
    }

    private Map<String, String> errorMessageMapJson(String exceptionMessage) {
        Map<String, String> result = new HashMap<>();
        result.put("errorMessage", exceptionMessage);
        return result;
    }
}
