package io.w4t3rcs.python.local;

/**
 * Interface defining the contract for handling processes.
 * Implementations of this interface process a Java Process object
 * and return a result of the specified type.
 *
 * @param <R> The type of result returned by the handler
 */
public interface ProcessHandler<R> {
    /**
     * Handles a process and returns a result.
     *
     * @param process The Java Process object to handle
     * @return The result of handling the process, of type R
     */
    R handle(Process process);
}
