package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.aspect.AsyncPythonAnnotationEvaluator;
import io.w4t3rcs.python.aspect.BasicPythonAnnotationEvaluator;
import io.w4t3rcs.python.aspect.PythonAfterAspect;
import io.w4t3rcs.python.aspect.PythonBeforeAspect;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * Configuration properties for Python aspect execution.
 *
 * <p>Defines when asynchronous execution should be applied for Python script calls
 * intercepted by Spring AOP aspects.</p>
 *
 * <p>Properties are bound from the application configuration using the prefix
 * {@code spring.python.aspect}.</p>
 *
 * <p><b>AsyncScopes values:</b></p>
 * <ul>
 *   <li>{@link PythonAspectProperties.AsyncProperties.Scope#BEFORE} — execute Python scripts asynchronously before the target method is invoked.</li>
 *   <li>{@link PythonAspectProperties.AsyncProperties.Scope#AFTER} — execute Python scripts asynchronously after the target method has completed.</li>
 * </ul>
 *
 * <p><b>Example (application.yml):</b></p>
 * <pre>{@code
 * spring:
 *   python:
 *     aspect:
 *       async:
 *         scopes: before, after
 *         core-pool-size: 10
 *         max-pool-size: 20
 *         queue-capacity: 50
 *         thread-name-prefix: AsyncPython-
 *         rejection-policy: caller_runs
 * }</pre>
 *
 * @param async properties defining async scopes and executor configuration, may be {@code null}
 * @see PythonBeforeAspect
 * @see PythonAfterAspect
 * @see BasicPythonAnnotationEvaluator
 * @see AsyncPythonAnnotationEvaluator
 * @author w4t3rcs
 * @since 1.0.0
 */
@ConfigurationProperties("spring.python.aspect")
public record PythonAspectProperties(AsyncProperties async) {
    /**
     * Properties for asynchronous execution configuration within Python aspect.
     *
     * <p>Includes the scopes when async execution applies and thread pool settings
     * for the async executor.</p>
     *
     * @param scopes array of async execution scopes, may be {@code null} or empty
     * @param corePoolSize core number of threads in the async executor thread pool
     * @param maxPoolSize maximum number of threads in the async executor thread pool
     * @param queueCapacity capacity of the async executor task queue
     * @param threadNamePrefix prefix used for naming async executor threads
     * @param rejectionPolicy {@link RejectedExecutionHandler} instance type
     */
    public record AsyncProperties(Scope[] scopes, int corePoolSize, int maxPoolSize, int queueCapacity, String threadNamePrefix, RejectionPolicy rejectionPolicy) {
        /**
         * Scopes defining when asynchronous execution of Python scripts is applied.
         */
        public enum Scope {
            BEFORE, AFTER
        }

        /**
         * Rejection policy defining what asynchronous should do on pool starvation.
         */
        public enum RejectionPolicy {
            CALLER_RUNS, ABORT, DISCARD, DISCARD_OLDEST
        }
    }
}
