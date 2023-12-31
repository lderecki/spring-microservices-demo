package pl.lderecki.simpleapiclient.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lderecki.simpleapiclient.DTO.*;
import pl.lderecki.simpleapiclient.service.ApiClientService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/index")
public class AppController {

    private final ApiClientService service;

    public AppController(ApiClientService service) {
        this.service = service;
    }


    @GetMapping
    public String getIndexPage(
            @RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            Model model) {

        model.addAttribute("dictEntityForm", new DictEntityFormDTO());


        Map<String, DictDTO> dicts = service.findAllDicts(authorizedClient);
        model.addAttribute("dicts", new ArrayList<DictDTO>(dicts.values()));

        List<DictEntityDTO> dictEntitiesRaw = new ArrayList<>();
        dicts.values().forEach(r -> dictEntitiesRaw.addAll(r.getEntities().values()));

        List<DictEntityTableReadDTO> dictEntities = dictEntitiesRaw.stream()
                .map(e -> new DictEntityTableReadDTO(e.getDictId(), dicts.get(e.getDictId()).getDictName(), e.getDictKey(), e.getDictValue()))
                .collect(Collectors.toList());
        model.addAttribute("dictEntities", dictEntities);

        model.addAttribute("entity", new SimpleEntityWriteDTO());

        List<DictEntityDTO> dict1 = new ArrayList<>(dicts.get("first_dict").getEntities().values());
        List<DictEntityDTO> dict2 = new ArrayList<>(dicts.get("second_dict").getEntities().values());
        model.addAttribute("dict1", dict1);
        model.addAttribute("dict2", dict2);

        //TODO do poprawy, pobieranie klucza ze słownika za pomocą wartości
        List<SimpleEntityReadDTO> entitiesRaw = service.findAllSimpleEntities(authorizedClient);
        List<SimpleEntityTableReadDTO> entities = entitiesRaw.stream()
                .map(e -> new SimpleEntityTableReadDTO(e.getId(),
                        dict1.stream().filter(d -> d.getDictValue().equals(e.getFirstDictionaryValue())).findFirst().orElseThrow().getDictKey(),
                        e.getFirstDictionaryValue(),
                        dict2.stream().filter(d2 -> d2.getDictValue().equals(e.getSecondDictionaryValue())).findFirst().orElseThrow().getDictKey(),
                        e.getSecondDictionaryValue(),
                        e.getSomeTextData()))
                .collect(Collectors.toList());
        model.addAttribute("entities", entities);

        return "index";
    }

    @PostMapping("/entity")
    public String postEntity(
            @RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            SimpleEntityWriteDTO toWrite) {

        service.saveSimpleEntity(authorizedClient, toWrite);

        return "redirect:/index";
    }

    @PostMapping("/entity/{id}")
    public String putEntity(
            @RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            SimpleEntityWriteDTO updateFrom,
            @PathVariable("id") long id) {

        updateFrom.setId(id);
        service.updateSimpleEntity(authorizedClient, updateFrom, updateFrom.getId());

        return "redirect:/index";
    }

    @PostMapping("/entity/delete/{id}")
    public String deleteEntity(
            @RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            @PathVariable("id") long id) {

        service.deleteSimpleEntity(authorizedClient, id);

        return "redirect:/index";
    }

    @PostMapping("/dict_entity")
    public String postDictEntity(
            @RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            DictEntityFormDTO toWrite) {

        service.saveDictEntity(authorizedClient, toWrite);

        return "redirect:/index";
    }

    @PostMapping("/dict_entity/{dictId}/{dictKey}")
    public String updateDictEntity(
            @RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            DictEntityFormDTO updateFrom) {

        service.updateDictEntity(authorizedClient, updateFrom);
        return "redirect:/index";
    }
}
