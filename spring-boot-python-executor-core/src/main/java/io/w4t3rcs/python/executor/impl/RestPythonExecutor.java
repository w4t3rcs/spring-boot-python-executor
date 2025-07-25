package io.w4t3rcs.python.executor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.dto.ScriptRequest;
import io.w4t3rcs.python.exception.PythonScriptExecutionException;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implementation of the {@link PythonExecutor} interface that executes Python scripts using REST endpoint.
 * This class coordinates the process of starting a Python process, handling its input
 * and error streams, and converting the result to the specified Java type.
 */
@Slf4j
@RequiredArgsConstructor
public class RestPythonExecutor implements PythonExecutor {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String USERNAME_HEADER = "X-Username";
    private static final String PASSWORD_HEADER = "X-Password";
    public static final String EMPTY_BODY = "\"\"";
    private final PythonExecutorProperties executorProperties;
    private final ObjectMapper objectMapper;

    @Override
    public <R> R execute(String script, Class<? extends R> resultClass) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            PythonExecutorProperties.RestProperties restProperties = executorProperties.rest();
            ScriptRequest scriptRequest = new ScriptRequest(script);
            String scriptJson = objectMapper.writeValueAsString(scriptRequest);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(restProperties.uri()))
                    .header(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE)
                    .header(USERNAME_HEADER, restProperties.username())
                    .header(PASSWORD_HEADER, restProperties.password())
                    .POST(HttpRequest.BodyPublishers.ofString(scriptJson))
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            String body = response.body();
            return resultClass == null || body == null || body.isBlank() || EMPTY_BODY.equals(body)
                    ? null
                    : objectMapper.readValue(body, resultClass);
        } catch (Exception e) {
            throw new PythonScriptExecutionException(e);
        }
    }
}
