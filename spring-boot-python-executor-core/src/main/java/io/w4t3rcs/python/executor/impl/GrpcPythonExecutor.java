package io.w4t3rcs.python.executor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.exception.PythonScriptExecutionException;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.proto.PythonRequest;
import io.w4t3rcs.python.proto.PythonResponse;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the {@link PythonExecutor} interface that executes Python scripts using gRPC endpoint.
 * This class coordinates the process of starting a Python process, handling its input
 * and error streams, and converting the result to the specified Java type.
 */
@Slf4j
@RequiredArgsConstructor
public class GrpcPythonExecutor implements PythonExecutor {
    private final PythonServiceGrpc.PythonServiceBlockingStub stub;
    private final ObjectMapper objectMapper;

    @Override
    public <R> R execute(String script, Class<? extends R> resultClass) {
        try {
            PythonResponse response = stub.sendCode(PythonRequest.newBuilder()
                    .setScript(script)
                    .build());
            String result = response.getResult();
            return objectMapper.readValue(result, resultClass);
        } catch (Exception e) {
            throw new PythonScriptExecutionException(e);
        }
    }
}
