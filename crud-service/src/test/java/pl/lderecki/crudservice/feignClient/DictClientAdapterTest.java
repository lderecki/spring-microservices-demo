package pl.lderecki.crudservice.feignClient;

import com.netflix.appinfo.InstanceInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DictClientAdapterTest {

    @Mock
    private DictClient client;

    @InjectMocks
    private DictClientAdapter testClass;

    @Test
    void translate() throws Exception{
        Field firstDict = testClass.getClass().getDeclaredField("FIRST_DICT");
        firstDict.setAccessible(true);
        firstDict.set(testClass, "first_dict");

        HashMap<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("dictId", "first_dict");
        expectedResponse.put("dictKey", "existing_key");
        expectedResponse.put("dictValue", "Test Value");

        HashMap<String, String> unexpectedResponse = new HashMap<>();
        unexpectedResponse.put("notExpectedKey", "notExpectedValue");

        when(client.translate("first_dict", "existing_key")).thenReturn(expectedResponse);
        when(client.translate("first_dict", "not_existing_key")).thenReturn(unexpectedResponse);
        when(client.translate("first_dict", "some_key")).thenReturn(null);

        assertEquals("not_existing_key", testClass.translate(testClass.FIRST_DICT, "not_existing_key"));

        assertEquals("Test Value", testClass.translate("first_dict", "existing_key"));

        assertEquals("some_key", testClass.translate("first_dict", "some_key"));
    }
}