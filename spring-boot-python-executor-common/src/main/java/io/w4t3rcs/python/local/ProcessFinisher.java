package io.w4t3rcs.python.local;

/**
 * Defines the contract for finalizing and cleaning up {@link Process} instances.
 *
 * <p>Implementations of this interface are responsible for ensuring that
 * a Java {@link Process} is properly terminated and all associated resources
 * (such as input/output streams) are released.
 * This may include waiting for the process to exit, forcibly terminating it if it does not stop within
 * a defined timeout, and performing any necessary logging or diagnostics.</p>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * Process process = new ProcessBuilder("python", "script.py").start();
 * ProcessFinisher finisher = ...;
 * finisher.finish(process);
 * }</pre>
 *
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface ProcessFinisher {
    /**
     * Finishes a running {@link Process}, performing all necessary cleanup operations.
     * <p>Implementations should ensure that:</p>
     * <ul>
     *     <li>The process is terminated (gracefully or forcibly)</li>
     *     <li>All associated input/output/error streams are closed</li>
     *     <li>Any system resources allocated to the process are released</li>
     * </ul>
     *
     * @param process non-{@code null} {@link Process} instance to finalize
     */
    void finish(Process process);
}
