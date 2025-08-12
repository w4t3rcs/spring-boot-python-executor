package io.w4t3rcs.python.local;

/**
 * Defines the contract for processing and handling {@link Process} instances.
 *
 * <p>Implementations of this interface encapsulate the logic for interacting
 * with a running or completed Java {@link Process}, extracting data from it,
 * performing analysis, or transforming its output into a body of the
 * specified generic type {@code R}.</p>
 *
 * <p>Common use cases include:</p>
 * <ul>
 *     <li>Reading and parsing the process's standard output or error streams</li>
 *     <li>Checking the exit code and mapping it to a domain-specific body</li>
 *     <li>Collecting logs, metrics, or diagnostic information from the process</li>
 * </ul>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * Process process = new ProcessBuilder("python", "script.py").start();
 * ProcessHandler<String> handler = ...;
 * String output = handler.handle(process);
 * }</pre>
 *
 * @param <R> the type of body returned by the handler
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface ProcessHandler<R> {
    /**
     * Processes the given {@link Process} and may produce a body.
     *
     * <p>Implementations should define the specific logic for interacting
     * with the process, which may involve reading its streams, checking
     * exit codes, or applying transformations.</p>
     *
     * @param process non-{@code null} {@link Process} instance to handle
     * @return the body of handling the process, of type {@code R}
     */
    R handle(Process process);
}