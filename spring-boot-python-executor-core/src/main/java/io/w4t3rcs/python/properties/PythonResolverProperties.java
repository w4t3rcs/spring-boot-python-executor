package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for choosing needed {@link PythonResolver} implementations and their configuration.
 * These properties allow the {@link PythonExecutor} bean to resolve the script properly before execution.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.resolver".</p>
 */
@ConfigurationProperties("spring.python.resolver")
public record PythonResolverProperties(DeclaredResolver[] declared, ResultProperties result, SpelythonProperties spelython, Py4JProperties py4j, RestrictedPythonProperties restrictedPython) {
    public enum DeclaredResolver {
        RESULT, SPELYTHON, PY4J, RESTRICTED_PYTHON
    }

    public record ResultProperties(String regex, String appearance, int positionFromStart, int positionFromEnd) {
    }

    public record SpelythonProperties(String regex, SpelProperties spel) {
        public record SpelProperties(String localVariableIndex, int positionFromStart, int positionFromEnd) {
        }
    }

    public record Py4JProperties(String importLine, String gateway) {
    }

    public record RestrictedPythonProperties(String importLine, String codeVariableName, String localVariablesName, String safeResultAppearance, String scriptImportsRegex, boolean printEnabled) {
    }
}
