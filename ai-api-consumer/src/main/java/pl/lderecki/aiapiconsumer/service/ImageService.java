package pl.lderecki.aiapiconsumer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pl.lderecki.aiapiconsumer.DTO.ImageResponseDTO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ImageService {

    private final WebClient webClient;

    private final String provider;
    private final String imageResolution;

    public ImageService(WebClient webClient, @Value("${api.ai-provider}") String provider,
                        @Value("${api.image-resolution}") String imageResolution) {
        this.webClient = webClient;
        this.provider = provider;
        this.imageResolution = imageResolution;
    }

    public byte[] postForImage(String description) {
        Map<String, String> body = new HashMap<>();
        body.put("providers", provider);
        body.put("resolution", imageResolution);
        body.put("text", description);
        body.put("response_as_dict", "false");
        body.put("show_original_response", "false");
        body.put("attributes_as_list", "true");
        body.put("num_images", "1");

        List<ImageResponseDTO> response = webClient.post()
                .uri(u -> u.pathSegment("image", "generation").build())
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ImageResponseDTO>>(){})
                .block();

        if (response == null)
            throw new IllegalStateException("Image reading error");

        byte[] image = Base64.getDecoder().decode(response.get(0).getImage().get(0));

        InputStream inputStream = new ByteArrayInputStream(image);
        byte[] byteArrayImageJpg;

        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            byteArrayImageJpg = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteArrayImageJpg;
    }
}

