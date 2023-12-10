package pl.lderecki.crudservice.restTemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DictRestTemplateTest{

    @Mock
    private RestTemplate rest;

    @InjectMocks
    DictRestTemplate testClass;

    @Test
    void translate() throws Exception{

        Field urlField = testClass.getClass().getDeclaredField("baseUrl");
        urlField.setAccessible(true);
        String url = "http://127.0.0.1:8081/dict_values";
        urlField.set(testClass, url);

        Field firstDict = testClass.getClass().getDeclaredField("FIRST_DICT");
        firstDict.setAccessible(true);
        firstDict.set(testClass, "first_dict");

        HashMap<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("dictId", "first_dict");
        expectedResponse.put("dictKey", "existing_key");
        expectedResponse.put("dictValue", "Test Value");

        HashMap<String, String> unexpectedResponse = new HashMap<>();
        unexpectedResponse.put("notExpectedKey", "notExpectedValue");

        URI existingUri = new URI("http://127.0.0.1:8081/dict_values/first_dict/existing_key");
        URI notExistingUri = new URI("http://127.0.0.1:8081/dict_values/first_dict/not_existing_key");
        URI notExpectedResponseUri = new URI("http://127.0.0.1:8081/dict_values/first_dict/some_key");

        when(rest.getForObject(existingUri, HashMap.class)).thenReturn(expectedResponse);
        when(rest.getForObject(notExistingUri, HashMap.class)).thenReturn(null);
        when(rest.getForObject(notExpectedResponseUri, HashMap.class)).thenReturn(unexpectedResponse);


        assertEquals("not_existing_key", testClass.translate(testClass.FIRST_DICT, "not_existing_key"));

        assertEquals("Test Value", testClass.translate("first_dict", "existing_key"));

        assertEquals("some_key", testClass.translate("first_dict", "some_key"));
    }
}