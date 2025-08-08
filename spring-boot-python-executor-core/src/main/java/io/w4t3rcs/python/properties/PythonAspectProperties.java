package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.aspect.AsyncPythonAnnotationEvaluator;
import io.w4t3rcs.python.aspect.BasicPythonAnnotationEvaluator;
import io.w4t3rcs.python.aspect.PythonAfterAspect;
import io.w4t3rcs.python.aspect.PythonBeforeAspect;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Python aspect execution.
 *
 * <p>Defines when asynchronous execution should be applied for Python script calls intercepted by Spring AOP aspects.
 * Properties are bound from the application configuration using the prefix {@code spring.python.aspect}.</p>
 *
 * <p><b>AsyncScope values:</b>
 * <ul>
 *   <li>{@link AsyncScope#BEFORE} — execute Python scripts asynchronously before the target method is invoked.</li>
 *   <li>{@link AsyncScope#AFTER} — execute Python scripts asynchronously after the target method has completed.</li>
 * </ul>
 * </p>
 *
 * <p><b>Example (application.yml):</b>
 * <pre>{@code
 * spring:
 *   python:
 *     aspect:
 *       asyncScopes: before, after
 * }</pre>
 * </p>
 *
 * @param asyncScopes array of {@link AsyncScope} values, may be {@code null} or empty
 * @see PythonBeforeAspect
 * @see PythonAfterAspect
 * @see BasicPythonAnnotationEvaluator
 * @see AsyncPythonAnnotationEvaluator
 * @author w4t3rcs
 * @since 1.0.0
 */
@ConfigurationProperties("spring.python.aspect")
public record PythonAspectProperties(AsyncScope[] asyncScopes) {
    public enum AsyncScope {
        BEFORE, AFTER
    }
}
