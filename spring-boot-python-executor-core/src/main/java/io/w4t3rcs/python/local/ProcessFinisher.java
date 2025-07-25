package io.w4t3rcs.python.local;

/**
 * Interface defining the contract for finishing processes.
 * Implementations of this interface handle the cleanup and termination
 * of Java Process objects.
 */
public interface ProcessFinisher {
    /**
     * Finishes a process, performing any necessary cleanup.
     * This typically involves ensuring the process is terminated properly
     * and any resources associated with it are released.
     *
     * @param process The Java Process object to finish
     */
    void finish(Process process);
}
