package io.w4t3rcs.python.resolver;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link PythonResolverHolder} that holds a list of {@link PythonResolver}
 * and applies them sequentially to a Python script.
 * <p>
 * This class enables step-by-step transformation of the script through a chain of resolvers.
 * It is immutable — the list of resolvers is provided via constructor and does not change.
 * </p>
 * <p>
 * Usage example:
 * <pre>{@code
 * List<PythonResolver> resolvers = List.of(new SpelythonResolver(...), new Py4JResolver(...), ...);
 * PythonResolverHolder holder = new BasicPythonResolverHolder(resolvers);
 * String script = "...";
 * Map<String, Object> args = Map.of("key", value);
 * String resolved = holder.resolveAll(script, args);
 * }</pre>
 * </p>
 *
 * @see PythonResolverHolder
 * @see PythonResolver
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class BasicPythonResolverHolder implements PythonResolverHolder {
    /**
     * The list of resolvers used to process the script sequentially.
     * Cannot be {@code null}. It is recommended to use an unmodifiable list.
     */
    private final List<PythonResolver> pythonResolvers;

    /**
     * Sequentially applies all {@link PythonResolver} instances from {@link #getResolvers()} to the input script.
     * <p>
     * Returns the final script after all resolvers have been applied in order.
     * <p>
     * Behavior for {@code arguments} depends on individual resolver implementations.
     * This method does not modify the original script but creates new string instances on each iteration.
     * </p>
     *
     * @param script the original Python script, must be non-null and non-empty
     * @param arguments map of arguments passed to resolvers, can be {@code null} or empty
     * @return the fully resolved script, never {@code null}
     * @throws IllegalArgumentException if {@code script} is {@code null} or empty
     */
    @Override
    public String resolveAll(String script, Map<String, Object> arguments) {
        if (script == null || script.isEmpty()) throw new IllegalArgumentException("Script cannot be null or empty");
        String resolvedScript = script;
        for (PythonResolver resolver : this.getResolvers()) {
            resolvedScript = resolver.resolve(resolvedScript, arguments);
        }
        return resolvedScript;
    }

    /**
     * Returns the list of resolvers registered in this holder.
     * <p>
     * The returned list may or may not be thread-safe or modifiable,
     * depending on the list passed to the constructor.
     * Modifications to the list outside this class will affect this holder's behavior.
     * </p>
     *
     * @return the list of {@link PythonResolver}, never {@code null}
     */
    @Override
    public List<PythonResolver> getResolvers() {
        return pythonResolvers;
    }
}