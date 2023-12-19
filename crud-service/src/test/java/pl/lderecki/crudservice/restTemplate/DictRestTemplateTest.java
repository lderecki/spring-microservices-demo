package pl.lderecki.crudservice.restTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DictRestTemplateTest{

    @Mock
    private RestTemplate rest;

    @Mock
    private EurekaClient eurekaClient;

    @InjectMocks
    DictRestTemplate testClass;

    @Test
    void translate() throws Exception{

        Field endpointField = testClass.getClass().getDeclaredField("endpoint");
        endpointField.setAccessible(true);
        String endpoint = "dict_values";
        endpointField.set(testClass, endpoint);

        Field serviceNameField = testClass.getClass().getDeclaredField("dictServiceName");
        serviceNameField.setAccessible(true);
        String serviceName = "dict-service";
        serviceNameField.set(testClass, serviceName);

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

        InstanceInfo mockInstanceInfo = mock(InstanceInfo.class);
        when(eurekaClient.getNextServerFromEureka("dict-service", false))
                .thenReturn(mockInstanceInfo);
        when(mockInstanceInfo.getHomePageUrl()).thenReturn("http://127.0.0.1:8081/");
        when(rest.getForObject(existingUri, HashMap.class)).thenReturn(expectedResponse);
        when(rest.getForObject(notExistingUri, HashMap.class)).thenReturn(null);
        when(rest.getForObject(notExpectedResponseUri, HashMap.class)).thenReturn(unexpectedResponse);

        assertEquals("not_existing_key", testClass.translate(testClass.FIRST_DICT, "not_existing_key"));

        assertEquals("Test Value", testClass.translate("first_dict", "existing_key"));

        assertEquals("some_key", testClass.translate("first_dict", "some_key"));
    }
}