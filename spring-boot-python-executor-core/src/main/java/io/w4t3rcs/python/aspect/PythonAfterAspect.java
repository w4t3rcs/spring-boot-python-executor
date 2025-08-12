package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonAfter;
import io.w4t3rcs.python.annotation.PythonAfters;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.lang.annotation.Annotation;
import java.util.HashMap;

/**
 * Aspect that intercepts method executions annotated with {@link PythonAfter}
 * or {@link PythonAfters} to evaluate associated Python scripts after successful method completion.
 * <p>
 * This aspect captures the returned value from the method and passes it as an additional argument
 * named {@code "body"} to the {@link PythonAnnotationEvaluator}.
 * <p>
 * The aspect listens to two pointcuts:
 * <ul>
 *   <li>Methods annotated with {@code @PythonAfter} — triggers a single script evaluation.</li>
 *   <li>Methods annotated with {@code @PythonAfters} — triggers multiple script evaluations.</li>
 * </ul>
 * <p>
 *
 * @see PythonAnnotationEvaluator
 * @see PythonAfter
 * @see PythonAfters
 * @see PythonBeforeAspect
 * @author w4t3rcs
 * @since 1.0.0
 */
@Aspect
@RequiredArgsConstructor
public class PythonAfterAspect {
    private final PythonAnnotationEvaluator annotationEvaluator;

    /**
     * Advice that executes after successful return of methods annotated with {@link PythonAfters}.
     * Passes the method return value as an additional argument named {@code "body"}.
     *
     * @param joinPoint non-null join point representing the intercepted method call
     * @param result the returned object from the intercepted method, may be null
     */
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.annotation.PythonAfters)", returning = "result")
    public void executeMultipleAfterMethod(JoinPoint joinPoint, Object result) {
        this.evaluateWithResult(joinPoint, result, PythonAfters.class);
    }

    /**
     * Advice that executes after successful return of methods annotated with {@link PythonAfter}.
     * Passes the method return value as an additional argument named {@code "body"}.
     *
     * @param joinPoint non-null join point representing the intercepted method call
     * @param result the returned object from the intercepted method; may be null
     */
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.annotation.PythonAfter)", returning = "result")
    public void executeSingleAfterMethod(JoinPoint joinPoint, Object result) {
        this.evaluateWithResult(joinPoint, result, PythonAfter.class);
    }

    /**
     * Helper method to invoke the {@link PythonAnnotationEvaluator} with the given join point,
     * annotation class, and additional arguments containing the method's return value under the key {@code "body"}.
     *
     * @param joinPoint non-null join point representing the intercepted method call
     * @param result the returned object from the intercepted method, may be null
     * @param annotation non-null class of the annotation to evaluate
     */
    private void evaluateWithResult(JoinPoint joinPoint, Object result, Class<? extends Annotation> annotation) {
        HashMap<String, Object> additionalArguments = new HashMap<>();
        if (result != null) additionalArguments.put("result", result);
        annotationEvaluator.evaluate(joinPoint, annotation, additionalArguments);
    }
}