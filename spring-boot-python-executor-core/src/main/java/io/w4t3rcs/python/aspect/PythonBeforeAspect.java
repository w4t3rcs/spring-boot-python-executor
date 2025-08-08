package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonBefore;
import io.w4t3rcs.python.annotation.PythonBefores;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Aspect that intercepts method executions annotated with {@link PythonBefore}
 * or {@link PythonBefores} to evaluate associated Python scripts before method invocation.
 * <p>
 * Delegates the evaluation logic to the injected {@link PythonAnnotationEvaluator}.
 * <p>
 * The aspect listens to two pointcuts:
 * <ul>
 *   <li>Methods annotated with {@code @PythonBefore} — triggers a single script evaluation.</li>
 *   <li>Methods annotated with {@code @PythonBefores} — triggers multiple script evaluations.</li>
 * </ul>
 * <p>
 *
 * @see PythonAnnotationEvaluator
 * @see PythonBefore
 * @see PythonBefores
 * @see PythonAfterAspect
 * @author w4t3rcs
 * @since 1.0.0
 */
@Aspect
@RequiredArgsConstructor
public class PythonBeforeAspect {
    private final PythonAnnotationEvaluator annotationEvaluator;

    /**
     * Advice that executes before methods annotated with {@link PythonBefores}.
     * Delegates evaluation of multiple Python scripts to {@link PythonAnnotationEvaluator}.
     *
     * @param joinPoint non-null join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.annotation.PythonBefores)")
    public void executeMultipleBeforeMethod(JoinPoint joinPoint) {
        annotationEvaluator.evaluate(joinPoint, PythonBefores.class);
    }

    /**
     * Advice that executes before methods annotated with {@link PythonBefore}.
     * Delegates evaluation of a single Python script to {@link PythonAnnotationEvaluator}.
     *
     * @param joinPoint non-null join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.annotation.PythonBefore)")
    public void executeSingleBeforeMethod(JoinPoint joinPoint) {
        annotationEvaluator.evaluate(joinPoint, PythonBefore.class);
    }
}