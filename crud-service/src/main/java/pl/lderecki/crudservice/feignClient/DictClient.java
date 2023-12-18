package pl.lderecki.crudservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient("${dicts.service.name}")
public interface DictClient {

    @GetMapping("/${dicts.service.endpoint}/{dictId}/{dictKey}")
    Map<String, String> translate(@PathVariable("dictId") String dictId, @PathVariable("dictKey") String dictKey);
}
