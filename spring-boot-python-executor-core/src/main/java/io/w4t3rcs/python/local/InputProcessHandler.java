package io.w4t3rcs.python.local;

import io.w4t3rcs.python.exception.PythonReadingException;
import io.w4t3rcs.python.executor.LocalPythonExecutor;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Processes and handles the standard output stream of a given {@link Process}.
 *
 * <p>This {@link ProcessHandler} implementation reads the standard output (stdout) of the process,
 * detects and extracts the result value marked by a configured appearance string from
 * {@link PythonResolverProperties.ResultProperties#appearance()}, and returns it as a raw JSON string.
 *
 * <p>If {@link PythonExecutorProperties.LocalProperties#loggable()} is enabled, all output lines
 * (including non-result lines) are logged at <code>INFO</code> level.
 *
 * <p>Example usage:
 * <pre>{@code
 * Process process = new ProcessBuilder("python", "script.py").start();
 * InputProcessHandler handler = new InputProcessHandler(executorProperties, resolverProperties);
 * String jsonResult = handler.handle(process);
 * if (jsonResult != null) {
 *     MyResult result = new ObjectMapper().readValue(jsonResult, MyResult.class);
 * }
 * }</pre>
 *
 * @see ProcessHandler
 * @see ErrorProcessHandler
 * @see PythonExecutorProperties
 * @see PythonResolverProperties
 * @see LocalPythonExecutor
 * @author w4t3rcs
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class InputProcessHandler implements ProcessHandler<String> {
    private final PythonExecutorProperties executorProperties;
    private final PythonResolverProperties resolverProperties;

    /**
     * Reads and processes the standard output stream of the specified {@link Process}.
     *
     * <p>Scans all output lines, detects the configured result marker, extracts the part
     * after the marker as a JSON string, and returns it.
     * Optionally logs all lines if enabled in {@link PythonExecutorProperties.LocalProperties}.
     *
     * @param process the non-{@code null} {@link Process} whose standard output should be handled
     * @return the extracted JSON result string, or {@code null} if no marker was found
     * @throws PythonReadingException if reading the standard output fails
     */
    @Override
    public String handle(Process process) {
        var localProperties = executorProperties.local();
        var resultProperties = resolverProperties.result();
        AtomicReference<String> result = new AtomicReference<>();
        try (BufferedReader bufferedReader = process.inputReader()) {
            bufferedReader.lines().forEach(line -> {
                if (line.contains(resultProperties.appearance())) {
                    String resultJson = line.replace(resultProperties.appearance(), "");
                    result.set(resultJson);
                }
                if (localProperties.loggable()) {
                    log.info(line);
                }
            });
        } catch (IOException e) {
            throw new PythonReadingException(e);
        }
        return result.get();
    }
}