package io.w4t3rcs.python.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import io.w4t3rcs.python.exception.PythonScriptExecutionException;
import io.w4t3rcs.python.proto.PythonRequest;
import io.w4t3rcs.python.proto.PythonResponse;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the {@link PythonExecutor} interface that executes Python scripts via a gRPC endpoint.
 * <p>
 * This class uses a gRPC blocking stub to send Python code for execution on a remote Python service.
 * It handles communication with the gRPC server, processes the response, and converts the
 * returned JSON result into the specified Java type.
 * <p>
 * This executor abstracts the complexity of starting and managing Python processes,
 * relying on the gRPC service to execute scripts and return results.
 * <p>
 * Usage example:
 * <pre>{@code
 * PythonExecutor executor = new GrpcPythonExecutor(stub, objectMapper);
 * String script = "print('Hello, World!')";
 * String result = executor.execute(script, String.class);
 * }</pre>
 *
 * @see PythonExecutor
 * @see PythonRequest
 * @see PythonResponse
 * @see PythonServiceGrpc.PythonServiceBlockingStub
 * @see PythonServerConnectionDetails
 * @see RestPythonExecutor
 * @see LocalPythonExecutor
 * @author w4t3rcs
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class GrpcPythonExecutor implements PythonExecutor {
    private final PythonServiceGrpc.PythonServiceBlockingStub stub;
    private final ObjectMapper objectMapper;

    /**
     * Executes the given Python {@code script} via the gRPC Python service and converts the result to the specified type.
     *
     * @param <R> the expected result type
     * @param script the Python script to execute (non-null, non-empty recommended)
     * @param resultClass the {@link Class} representing the expected type of the result, may be null if no result expected
     * @return an instance of {@code R} parsed from the Python script output, or {@code null} if {@code resultClass} is null or output is blank
     * @throws PythonScriptExecutionException if any error occurs during script execution or result parsing
     */
    @Override
    public <R> R execute(String script, Class<? extends R> resultClass) {
        try {
            PythonResponse response = stub.sendCode(PythonRequest.newBuilder()
                    .setScript(script)
                    .build());
            String result = response.getResult();
            return resultClass == null || result.isBlank()
                    ? null
                    : objectMapper.readValue(result, resultClass);
        } catch (Exception e) {
            throw new PythonScriptExecutionException(e);
        }
    }
}
