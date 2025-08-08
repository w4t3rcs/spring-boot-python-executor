package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * Configuration properties for selecting and configuring {@link PythonResolver} implementations.
 *
 * <p>These properties enable the {@link PythonExecutor} bean to correctly resolve Python scripts
 * before execution by specifying which resolvers to use and their settings.</p>
 *
 * <p>Properties are bound from the application configuration using the prefix
 * {@code spring.python.resolver}.</p>
 *
 * <p><b>Example (application.yml):</b>
 * <pre>{@code
 * spring:
 *   python:
 *     resolver:
 *       declared: spelython, result
 *       script-imports-regex: (^import [\\w.]+$)|(^import [\\w.]+ as [\\w.]+$)|(^from [\\w.]+ import [\\w., ]+$)
 *       spelython:
 *         regex: spel\\{.+?}
 *         spel:
 *           localVariableIndex: "#"
 *           positionFromStart: 5
 *           positionFromEnd: 1
 *       py4j:
 *         import-line: "from py4j.java_gateway import JavaGateway, GatewayParameters"
 *         gateway-object: gateway = JavaGateway(\n\tgateway_parameters=GatewayParameters(\n\t\t%s\n\t)\n)
 *         gateway-properties[0]: address=\"${spring.python.py4j.host}\"
 *         gateway-properties[1]: port=${spring.python.py4j.port}
 *         gateway-properties[2]: auth_token=\"${spring.python.py4j.auth-token}\"
 *         gateway-properties[3]: auto_convert=True
 *       restrictedPython:
 *         import-line: from RestrictedPython import compile_restricted
 *         code-variable-name: source_code
 *         local-variables-name: execution_result
 *         safe-result-appearance: r4java_restricted
 *         print-enabled: true
 *       result:
 *         regex: o4java\\{.+?}
 *         appearance: r4java
 *         position-from-start: 7
 *         position-from-end: 1
 * }</pre>
 * </p>
 *
 * @param declared the list of declared resolvers to be used; non-null, may be empty
 * @param scriptImportsRegex regular expression for matching import statements in scripts; non-null
 * @param spelython configuration properties specific to Spelython resolver; non-null
 * @param py4j configuration properties specific to Py4J resolver; non-null
 * @param restrictedPython configuration properties specific to Restricted Python resolver; non-null
 * @param result configuration properties for result parsing; non-null
 * @see PythonResolver
 * @see PythonResolverHolder
 * @author w4t3rcs
 * @since 1.0.0
 */
@ConfigurationProperties("spring.python.resolver")
public record PythonResolverProperties(DeclaredResolver[] declared, String scriptImportsRegex, SpelythonProperties spelython, Py4JProperties py4j, RestrictedPythonProperties restrictedPython, ResultProperties result) {
    public enum DeclaredResolver {
        SPELYTHON, PY4J, RESTRICTED_PYTHON, RESULT, PRINTED_RESULT
    }

    /**
     * Configuration properties for the Spelython resolver.
     *
     * @param regex the regex pattern to match SpEL expressions, non-null
     * @param spel nested properties related to SpEL expression parsing, non-null
     * @see SpelythonResolver
     */
    public record SpelythonProperties(String regex, SpelProperties spel) {
        /**
         * Properties specifying details for SpEL expression parsing.
         *
         * @param localVariableIndex the name of the local variable index, non-null
         * @param positionFromStart the number of characters to skip from start, >= 0
         * @param positionFromEnd the number of characters to skip from end, >= 0
         */
        public record SpelProperties(String localVariableIndex, int positionFromStart, int positionFromEnd) {
        }
    }

    /**
     * Configuration properties for the Py4J resolver.
     *
     * @param importLine the import line required for Py4J integration, non-null
     * @param gatewayObject the name of the Py4J gateway object, non-null
     * @param gatewayProperties array of gateway property names, non-null, may be empty
     * @see Py4JResolver
     */
    public record Py4JProperties(String importLine, String gatewayObject, String[] gatewayProperties) {
    }

    /**
     * Configuration properties for the Restricted Python resolver.
     *
     * @param importLine the import line required for Restricted Python integration, non-null
     * @param codeVariableName the variable name holding the code string, non-null
     * @param localVariablesName the variable name holding local variables map, non-null
     * @param safeResultAppearance the marker for safe results in output, non-null
     * @param printEnabled whether printing is enabled for restricted code execution
     * @see RestrictedPythonResolver
     */
    public record RestrictedPythonProperties(String importLine, String codeVariableName, String localVariablesName, String safeResultAppearance, boolean printEnabled) {
    }

    /**
     * Configuration properties describing how to extract results from Python execution output.
     *
     * @param regex regular expression to match result lines, non-null
     * @param appearance prefix string indicating result presence, non-null
     * @param positionFromStart number of characters to skip from start, >= 0
     * @param positionFromEnd number of characters to skip from end, >= 0
     * @see ResultResolver
     */
    public record ResultProperties(String regex, String appearance, int positionFromStart, int positionFromEnd) {
    }
}
