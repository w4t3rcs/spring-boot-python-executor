package io.w4t3rcs.python.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import io.w4t3rcs.python.dto.ScriptRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static io.w4t3rcs.python.constant.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class RestPythonExecutorTests {
    @InjectMocks
    private RestPythonExecutor restPythonExecutor;
    @Mock
    private PythonServerConnectionDetails connectionDetails;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpClient client;
    @Mock
    private HttpResponse<String> response;

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3})
    void testExecute(String script) {
        ScriptRequest scriptRequest = new ScriptRequest(script);

        Mockito.when(objectMapper.writeValueAsString(scriptRequest)).thenReturn("{\"script\": \"%s\"}".formatted(script));
        Mockito.when(connectionDetails.getUri()).thenReturn("http://localhost:8000/script");
        Mockito.when(connectionDetails.getUsername()).thenReturn("username");
        Mockito.when(connectionDetails.getToken()).thenReturn("token");
        Mockito.when(client.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class))).thenReturn(response);
        Mockito.when(response.body()).thenReturn(OK);
        Mockito.when((String) objectMapper.readValue(OK, STRING_CLASS)).thenReturn(OK);

        String executed = restPythonExecutor.execute(script, STRING_CLASS);
        Assertions.assertEquals(OK, executed);
    }
}
