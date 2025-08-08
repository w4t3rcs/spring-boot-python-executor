package io.w4t3rcs.python.local;

import io.w4t3rcs.python.exception.ProcessStartException;
import io.w4t3rcs.python.executor.LocalPythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Starts Python processes from script files or inline Python code.
 *
 * <p>This {@link ProcessStarter} implementation constructs and launches a {@link Process}
 * using the configured Python start command from {@link PythonExecutorProperties}.
 * Depending on the input, it will either:
 * <ul>
 *     <li>Execute a Python script from a file path (if {@link PythonFileHandler#isPythonFile(String)} returns {@code true}).</li>
 *     <li>Execute inline Python code using the {@code -c} option.</li>
 * </ul>
 *
 * <p>If inline code contains double quotes, they are escaped by doubling them to ensure
 * proper command-line parsing.</p>
 *
 * <p><b>Execution order:</b> After starting the process via {@link ProcessBuilder#start()},
 * this implementation waits for its completion using {@link Process#waitFor()} before returning.
 * As a result, the returned process is always in a terminated state.</p>
 *
 * <p>Example usage:
 * <pre>{@code
 * ProcessStarter starter = new ProcessStarterImpl(executorProperties, pythonFileHandler);
 *
 * // Start from a Python file
 * Process fileProcess = starter.start("/path/to/script.py");
 *
 * // Start from inline code
 * Process inlineProcess = starter.start("print('Hello from inline Python')");
 * }</pre>
 *
 * @see ProcessStarter
 * @see PythonExecutorProperties
 * @see PythonFileHandler
 * @see LocalPythonExecutor
 * @author w4t3rcs
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class ProcessStarterImpl implements ProcessStarter {
    private static final String COMMAND_HEADER = "-c";
    private final PythonExecutorProperties executorProperties;
    private final PythonFileHandler pythonFileHandler;

    /**
     * Starts a Python process from either a file or inline code.
     *
     * @param script non-{@code null} Python script, can be a file path or inline code
     * @return non-{@code null} terminated {@link Process} representing the executed script
     * @throws ProcessStartException if the process cannot be started or interrupted
     */
    @Override
    public Process start(String script) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String startCommand = executorProperties.local().startCommand();
            if (pythonFileHandler.isPythonFile(script)) {
                processBuilder.command(startCommand, pythonFileHandler.getScriptPath(script).toString());
            } else {
                processBuilder.command(startCommand, COMMAND_HEADER, script.replace("\"", "\"\""));
            }

            log.info("Python script is going to be executed");
            Process process = processBuilder.start();
            process.waitFor();
            return process;
        } catch (Exception e) {
            throw new ProcessStartException(e);
        }
    }
}