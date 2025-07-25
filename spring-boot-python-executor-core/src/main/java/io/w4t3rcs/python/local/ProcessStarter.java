package io.w4t3rcs.python.local;

/**
 * Interface defining the contract for starting processes.
 * Implementations of this interface create and start a new process
 * based on the provided command.
 */
public interface ProcessStarter {
    /**
     * Starts a new process with the specified command.
     *
     * @param command The command to execute in the new process
     * @return The Java Process object representing the started process
     * @throws RuntimeException if the process cannot be started
     */
    Process start(String command);
}
