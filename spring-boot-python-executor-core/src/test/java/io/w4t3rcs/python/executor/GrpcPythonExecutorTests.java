package io.w4t3rcs.python.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.proto.PythonRequest;
import io.w4t3rcs.python.proto.PythonResponse;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.w4t3rcs.python.constant.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class GrpcPythonExecutorTests {
    @InjectMocks
    private GrpcPythonExecutor grpcPythonExecutor;
    @Mock
    private PythonServiceGrpc.PythonServiceBlockingStub stub;
    @Mock
    private ObjectMapper objectMapper;

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3})
    void testExecute(String script) {
        PythonRequest scriptRequest = PythonRequest.newBuilder()
                .setScript(script)
                .build();
        PythonResponse scriptResponse = PythonResponse.newBuilder()
                .setResult(OK)
                .build();

        Mockito.when(stub.sendCode(scriptRequest)).thenReturn(scriptResponse);
        Mockito.when((String) objectMapper.readValue(OK, OK_CLASS)).thenReturn(OK);

        String executed = grpcPythonExecutor.execute(script, OK_CLASS);
        Assertions.assertEquals(OK, executed);
    }
}
