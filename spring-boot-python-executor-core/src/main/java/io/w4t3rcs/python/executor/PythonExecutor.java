package io.w4t3rcs.python.executor;

/**
 * Interface defining the contract for executing Python scripts.
 * Implementations of this interface handle the actual execution of Python code
 * and conversion of the results to Java objects.
 */
public interface PythonExecutor {
    /**
     * Executes a Python script and converts the result to the specified Java type.
     *
     * @param <R> The type of result expected from the script execution
     * @param script The Python script to execute
     * @param resultClass The class representing the expected result type
     * @return The result of the script execution, cast to the specified result class
     */
    <R> R execute(String script, Class<? extends R> resultClass);
}
