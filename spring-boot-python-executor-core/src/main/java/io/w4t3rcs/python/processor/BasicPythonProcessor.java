package io.w4t3rcs.python.processor;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolverHolder;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Default implementation of {@link PythonProcessor} that provides the basic
 * processing workflow for executing Python scripts with optional argument resolution.
 *
 * <p>This implementation integrates three main components:
 * <ul>
 *     <li>{@link PythonFileHandler} – detects if the given script is a file and, if so,
 *         reads its contents.</li>
 *     <li>{@link PythonResolverHolder} – applies all registered resolvers to the script
 *         (e.g., resolves placeholders or SpEL expressions) using the provided arguments.</li>
 *     <li>{@link PythonExecutor} – executes the fully resolved Python script and
 *         converts the result to the requested Java type.</li>
 * </ul>
 *
 * <p><b>Workflow:</b>
 * <ol>
 *     <li>If {@code script} is a Python file path, load its contents.</li>
 *     <li>Apply argument-based resolution to the script text.</li>
 *     <li>Execute the resolved script and return the execution result.</li>
 * </ol>
 *
 * <p>Example usage:
 * <pre>{@code
 * BasicPythonProcessor processor = new BasicPythonProcessor(fileHandler, executor, resolverHolder);
 * String result = processor.process("print('Hello')", String.class, Map.of());
 * }</pre>
 *
 * @see PythonProcessor
 * @see PythonExecutor
 * @see PythonFileHandler
 * @see PythonResolverHolder
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class BasicPythonProcessor implements PythonProcessor {
    private final PythonFileHandler pythonFileHandler;
    private final PythonExecutor pythonExecutor;
    private final PythonResolverHolder pythonResolverHolder;

    /**
     * Processes a Python script by optionally reading it from a file,
     * applying all resolvers, and executing it.
     *
     * @param script non-{@code null} Python script content or file path
     * @param resultClass nullable target result type
     * @param arguments optional arguments for resolvers (can be empty but not {@code null})
     * @param <R> type of the expected result
     * @return the execution result converted to {@code resultClass}
     */
    @Override
    public <R> R process(String script, Class<? extends R> resultClass, Map<String, Object> arguments) {
        String resolvedScript = script;
        if (pythonFileHandler.isPythonFile(script)) resolvedScript = pythonFileHandler.readScriptBodyFromFile(script);
        resolvedScript = pythonResolverHolder.resolveAll(resolvedScript, arguments);
        return pythonExecutor.execute(resolvedScript, resultClass);
    }
}
