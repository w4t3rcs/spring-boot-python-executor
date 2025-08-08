package io.w4t3rcs.python.local;

/**
 * Defines the contract for creating and starting {@link Process} instances.
 *
 * <p>Implementations of this interface are responsible for constructing
 * and launching new operating system processes, typically for executing
 * scripts, commands, or external programs.</p>
 *
 * <p>Common use cases include:</p>
 * <ul>
 *     <li>Starting an interpreter process (e.g., Python)</li>
 *     <li>Launching compiled executables</li>
 *     <li>Spawning background worker processes</li>
 * </ul>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * ProcessStarter starter = ...;
 * Process process = starter.start("print('Hello from Python')");
 * }</pre>
 *
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface ProcessStarter {
    /**
     * Starts a new {@link Process} using the given script or command.
     *
     * <p>The {@code script} argument represents either the inline code to be executed by an interpreter
     *
     * @param script non-{@code null} script or command to execute
     * @return the started {@link Process} instance
     */
    Process start(String script);
}
