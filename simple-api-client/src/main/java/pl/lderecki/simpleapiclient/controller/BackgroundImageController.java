package pl.lderecki.simpleapiclient.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.lderecki.simpleapiclient.DTO.ImageFormDTO;
import pl.lderecki.simpleapiclient.controller.session.SessionBackground;
import pl.lderecki.simpleapiclient.service.ApiClientService;

@RestController
@RequestMapping("/background_image")
public class BackgroundImageController {

    private final ApiClientService service;
    private final SessionBackground sessionBackground;

    public BackgroundImageController(ApiClientService service, SessionBackground sessionBackground) {
        this.service = service;
        this.sessionBackground = sessionBackground;
    }

    @GetMapping
    public ModelAndView getForm(Model model) {

        model.addAttribute("imageForm", new ImageFormDTO());
        return new ModelAndView("background_form");
    }

    @PostMapping
    public ModelAndView postForm(@RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
                          ImageFormDTO imageForm) {

        System.out.println(imageForm);
        byte[] byteArrayImage = service.getImageWithDescription(authorizedClient, imageForm.getImageDescription());

        sessionBackground.setImage(byteArrayImage);

        return new ModelAndView("redirect:/index");
    }

    @GetMapping(value = "/image")
    public ResponseEntity<Resource> getImage() {
        ByteArrayResource image =  new ByteArrayResource(sessionBackground.getImage());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(image.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename("image.jpg")
                                .build().toString())
                .body(image);
    }
}
