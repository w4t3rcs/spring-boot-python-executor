package io.w4t3rcs.python.config;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for choosing needed {@link PythonResolver} implementations and their configuration.
 * These properties allow the {@link PythonExecutor} bean to resolve the script properly before execution.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.resolver".</p>
 */
@ConfigurationProperties("spring.python.resolver")
public record PythonResolverProperties(@DefaultValue("result, spelython") DeclaredResolver[] declared, ResultProperties result, SpelythonProperties spelython, Py4JProperties py4j, RestrictedPythonProperties restrictedPython) {
    public enum DeclaredResolver {
        RESULT, SPELYTHON, PY4J, RESTRICTED_PYTHON
    }

    public record ResultProperties(@DefaultValue("o4java\\{.+?}") String regex,
                                   @DefaultValue("r4java") String appearance,
                                   @DefaultValue("7") int positionFromStart,
                                   @DefaultValue("1") int positionFromEnd) {
    }

    public record SpelythonProperties(@DefaultValue("spel\\{.+?}") String regex, SpelProperties spel) {
        public record SpelProperties(@DefaultValue("#") String localVariableIndex,
                                     @DefaultValue("5") int positionFromStart,
                                     @DefaultValue("1") int positionFromEnd) {
        }
    }

    public record Py4JProperties(@DefaultValue("from py4j.java_gateway import JavaGateway") String importLine,
                                 @DefaultValue("gateway = JavaGateway()") String gateway) {
    }

    public record RestrictedPythonProperties(@DefaultValue("from RestrictedPython import compile_restricted\nfrom RestrictedPython import safe_globals") String importLine,
                                             @DefaultValue("source_code") String codeVariableName,
                                             @DefaultValue("execution_result") String localVariablesName,
                                             @DefaultValue("r4java_restricted") String safeResultAppearance) {
    }
}
