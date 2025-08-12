package io.w4t3rcs.python.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.dto.PythonExecutionResponse;
import io.w4t3rcs.python.exception.PythonScriptExecutionException;
import io.w4t3rcs.python.local.ProcessFinisher;
import io.w4t3rcs.python.local.ProcessHandler;
import io.w4t3rcs.python.local.ProcessStarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the {@link PythonExecutor} interface that executes Python scripts locally.
 * <p>
 * This class manages the lifecycle of a local Python process by:
 * <ul>
 *   <li>Starting the Python process with the provided script using {@link ProcessStarter}.</li>
 *   <li>Handling the process's input stream to capture the Python script output via {@link ProcessHandler}.</li>
 *   <li>Handling the process's error stream to capture error messages via {@link ProcessHandler}.</li>
 *   <li>Ensuring the process completes correctly using {@link ProcessFinisher}.</li>
 *   <li>Converting the captured JSON output into the specified Java type.</li>
 * </ul>
 * <p>
 * Usage example:
 * <pre>{@code
 * PythonExecutor executor = new LocalPythonExecutor(processStarter, inputHandler, errorHandler, objectMapper, processFinisher);
 * String script = "print('Hello from Python')";
 * String body = executor.execute(script, String.class);
 * }</pre>
 *
 * @see PythonExecutor
 * @see ProcessStarter
 * @see ProcessHandler
 * @see ProcessFinisher
 * @see RestPythonExecutor
 * @see GrpcPythonExecutor
 * @author w4t3rcs
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class LocalPythonExecutor implements PythonExecutor {
    private final ProcessStarter processStarter;
    private final ProcessHandler<String> inputProcessHandler;
    private final ProcessHandler<Void> errorProcessHandler;
    private final ObjectMapper objectMapper;
    private final ProcessFinisher processFinisher;

    /**
     * Executes the provided Python {@code script} locally, captures the JSON output,
     * and converts it into an instance of the specified {@code resultClass}.
     * <p>
     * The method starts a new Python process, processes the output and error streams,
     * and waits for the process termination.
     *
     * @param <R> the expected body type
     * @param script the Python script to execute (non-null, non-empty recommended)
     * @param resultClass the {@link Class} representing the expected return type, may be null if no body expected
     * @return an instance of {@code R} parsed from the Python script output, or {@code null} if {@code resultClass} is null or output is blank
     * @throws PythonScriptExecutionException if an error occurs during process execution, I/O handling, or JSON deserialization
     */
    @Override
    public <R> PythonExecutionResponse<R> execute(String script, Class<? extends R> resultClass) {
        try {
            Process process = processStarter.start(script);
            String jsonResult = inputProcessHandler.handle(process);
            errorProcessHandler.handle(process);
            processFinisher.finish(process);
            R result = resultClass == null || jsonResult == null || jsonResult.isBlank()
                    ? null
                    : objectMapper.readValue(jsonResult, resultClass);
            return new PythonExecutionResponse<>(result);
        } catch (Exception e) {
            throw new PythonScriptExecutionException(e);
        }
    }
}
