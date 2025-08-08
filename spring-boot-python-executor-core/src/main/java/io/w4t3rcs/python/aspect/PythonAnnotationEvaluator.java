package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Interface defining the contract for evaluating Python-related annotations on methods.
 * <p>
 * Implementations perform resolution and execution of Python scripts specified
 * by annotations present on the intercepted method represented by the {@link JoinPoint}.
 * </p>
 * <p>
 * Evaluation may involve extracting script content, resolving parameters,
 * checking active profiles, and executing the scripts.
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * PythonAnnotationEvaluator evaluator = ...;
 * evaluator.evaluate(joinPoint, PythonAfter.class);
 * }</pre>
 *
 * @see BasicPythonAnnotationEvaluator
 * @see AsyncPythonAnnotationEvaluator
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface PythonAnnotationEvaluator {
    /**
     * Evaluates the specified annotation on the method represented by the given {@link JoinPoint}.
     * <p>
     * This default method invokes {@link #evaluate(JoinPoint, Class, Map)} with
     * an empty {@link Map} of additional arguments.
     * </p>
     *
     * @param <A> the type of annotation to evaluate, must extend {@link Annotation}
     * @param joinPoint the AOP join point representing the intercepted method, must not be {@code null}
     * @param annotationClass the {@link Class} object of the annotation to evaluate, must not be {@code null}
     */
    default <A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass) {
        evaluate(joinPoint, annotationClass, Map.of());
    }

    /**
     * Evaluates the specified annotation on the method represented by the given {@link JoinPoint}
     * using the provided additional arguments.
     * <p>
     * Implementations must process the Python scripts or expressions defined by the annotation,
     * using the {@code additionalArguments} as contextual data during evaluation.
     * </p>
     *
     * @param <A> the type of annotation to evaluate, must extend {@link Annotation}
     * @param joinPoint the AOP join point representing the intercepted method, must not be {@code null}
     * @param annotationClass the {@link Class} object of the annotation to evaluate, must not be {@code null}
     * @param additionalArguments additional context parameters for script evaluation, must not be {@code null}, may be empty
     */
    <A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass, Map<String, Object> additionalArguments);
}
