package io.w4t3rcs.python.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * {@link PythonAnnotationEvaluator} implementation that delegates evaluation
 * of Python-related annotations asynchronously.
 * <p>
 * This class wraps another {@link PythonAnnotationEvaluator} and executes
 * its {@code evaluate} method in a separate thread using {@link CompletableFuture},
 * allowing non-blocking behavior.
 * </p>
 * <p>
 * Note that the evaluation is performed asynchronously without any
 * completion handling or error propagation.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * PythonAnnotationEvaluator asyncEvaluator = new AsyncPythonAnnotationEvaluator(basicEvaluator);
 * asyncEvaluator.evaluate(joinPoint, PythonAfter.class);
 * }</pre>
 *
 * @see PythonAnnotationEvaluator
 * @see BasicPythonAnnotationEvaluator
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class AsyncPythonAnnotationEvaluator implements PythonAnnotationEvaluator {
    private final PythonAnnotationEvaluator annotationEvaluator;

    /**
     * Evaluates the specified Python-related annotation asynchronously.
     *
     * @param <A> the type of annotation to evaluate, must be a subtype of {@link Annotation}
     * @param joinPoint the AOP join point representing the intercepted method, must not be {@code null}
     * @param annotationClass the {@link Class} object of the annotation type to evaluate, must not be {@code null}
     * @param additionalArguments additional arguments to pass to the evaluator, must not be {@code null}
     */
    @Override
    public <A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass, Map<String, Object> additionalArguments) {
        CompletableFuture.runAsync(() -> annotationEvaluator.evaluate(joinPoint, annotationClass, additionalArguments));
    }
}
