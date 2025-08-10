package io.w4t3rcs.python.local;

import io.w4t3rcs.python.executor.LocalPythonExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles completion of a {@link Process} by checking its exit code and logging the result.
 *
 * <p>This {@link ProcessFinisher} implementation inspects the process's exit value:
 * <ul>
 *     <li>If the exit code is <code>0</code> — logs a success message at <code>INFO</code> level.</li>
 *     <li>Otherwise — logs an error message at <code>ERROR</code> level.</li>
 * </ul>
 * After logging, the process is explicitly terminated via {@link Process#destroy()} to release resources.
 *
 * <p><b>Error handling:</b>
 *
 * <p>Example usage:
 * <pre>{@code
 * Process process = new ProcessBuilder("python", "script.py").start();
 * process.waitFor(); // Ensure process completion before finishing
 * new ProcessFinisherImpl().finish(process);
 * }</pre>
 *
 * @see ProcessFinisher
 * @see LocalPythonExecutor
 * @author w4t3rcs
 * @since 1.0.0
 */
@Slf4j
public class BasicPythonProcessFinisher implements ProcessFinisher {
    /**
     * Finalizes the given {@link Process} by logging its exit status and releasing resources.
     *
     * @param process the non-{@code null} {@link Process} to finish; must be already terminated
     * @throws IllegalThreadStateException if the process has not yet terminated
     */
    @Override
    public void finish(Process process) {
        int exitCode = process.exitValue();
        if (exitCode == 0) {
            log.info("Python script is executed with code: {}", exitCode);
        } else {
            log.error("Something went wrong! Python script is executed with code: {}", exitCode);
        }
        process.destroy();
    }
}