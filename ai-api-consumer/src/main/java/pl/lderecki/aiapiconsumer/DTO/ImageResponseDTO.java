package pl.lderecki.aiapiconsumer.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ImageResponseDTO {

    private String status;
    private String provider;
    private float cost;
    private List<String> image;
    private List<String> imageResourceUrl;
}
