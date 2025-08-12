package io.w4t3rcs.python.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import io.w4t3rcs.python.dto.PythonExecutionResponse;
import io.w4t3rcs.python.dto.ScriptRequest;
import io.w4t3rcs.python.exception.PythonScriptExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implementation of the {@link PythonExecutor} interface that executes Python scripts via a REST endpoint.
 * <p>
 * This class sends the Python script to a remote REST service using HTTP POST with JSON payload,
 * then processes the response and converts it to the specified Java type.
 * <p>
 * The execution flow includes:
 * <ul>
 *   <li>Serializing the Python script wrapped in a {@link ScriptRequest} to JSON.</li>
 *   <li>Sending an HTTP POST request to the configured REST endpoint with authentication headers.</li>
 *   <li>Receiving the JSON response and deserializing it into the expected body type.</li>
 * </ul>
 * <p>
 * Usage example:
 * <pre>{@code
 * PythonExecutor executor = new RestPythonExecutor(connectionDetails, objectMapper, httpClient);
 * String script = "print('Hello from Python via REST')";
 * String body = executor.execute(script, String.class);
 * }</pre>
 *
 * @see PythonExecutor
 * @see ScriptRequest
 * @see PythonServerConnectionDetails
 * @see GrpcPythonExecutor
 * @see LocalPythonExecutor
 * @author w4t3rcs
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class RestPythonExecutor implements PythonExecutor {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String TOKEN_HEADER = "X-Token";
    public static final String EMPTY_BODY = "\"\"";
    private final PythonServerConnectionDetails connectionDetails;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    /**
     * Executes the given Python {@code script} remotely by sending it to a REST endpoint.
     * <p>
     * The method serializes the script into a JSON body, sends it as an HTTP POST request,
     * and deserializes the JSON response into the specified {@code resultClass}.
     *
     * @param <R> the expected body type
     * @param script the Python script to execute (non-null, non-empty recommended)
     * @param resultClass the {@link Class} representing the expected return type, may be null if no body expected
     * @return an instance of {@code R} parsed from the REST response body, or {@code null} if {@code resultClass} is null, or the response body is empty or blank
     * @throws PythonScriptExecutionException if an error occurs during HTTP communication, JSON serialization/deserialization, or other execution errors
     */
    @Override
    public <R> PythonExecutionResponse<R> execute(String script, Class<? extends R> resultClass) {
        try {
            ScriptRequest scriptRequest = new ScriptRequest(script);
            String scriptJson = objectMapper.writeValueAsString(scriptRequest);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(connectionDetails.getUri()))
                    .header(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE)
                    .header(TOKEN_HEADER, connectionDetails.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(scriptJson))
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, handler);
            if (response.statusCode() != HttpStatus.OK.value()) throw new PythonScriptExecutionException("Request failed with status code: " + response.statusCode());
            String body = response.body();
            R result = resultClass == null || body == null || body.isBlank() || EMPTY_BODY.equals(body)
                    ? null
                    : objectMapper.readValue(body, resultClass);
            return new PythonExecutionResponse<>(result);
        } catch (Exception e) {
            throw new PythonScriptExecutionException(e);
        }
    }
}