package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonAfter;
import io.w4t3rcs.python.annotation.PythonAfters;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;

/**
 * Aspect that handles the execution of Python scripts through annotations.
 * This aspect intercepts methods annotated with {@link PythonAfters} and {@link PythonAfter}
 * annotations and executes the specified Python scripts after the method execution.
 */
@Aspect
@RequiredArgsConstructor
public class PythonAfterAspect {
    private final PythonAnnotationEvaluator annotationEvaluator;

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfters}.
     * This advice intercepts method calls and executes the Python scripts specified
     * in the annotation after the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @After("@annotation(io.w4t3rcs.python.annotation.PythonAfters)")
    public void executeMultipleAfterMethod(JoinPoint joinPoint) {
        annotationEvaluator.evaluate(joinPoint, PythonAfters.class);
    }

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfters} return a result.
     * This advice intercepts method calls and executes the Python scripts specified
     * in the annotation after the method returns.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @param result The value returned by the method
     */
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.annotation.PythonAfters)", returning = "result")
    public void executeMultipleAfterReturningMethod(JoinPoint joinPoint, Object result) {
        annotationEvaluator.evaluate(joinPoint, PythonAfters.class, Map.of("result", result));
    }

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfter}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation after the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @After("@annotation(io.w4t3rcs.python.annotation.PythonAfter)")
    public void executeSingleAfterMethod(JoinPoint joinPoint) {
        annotationEvaluator.evaluate(joinPoint, PythonAfter.class);
    }

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfter} return a result.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation after the method returns.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @param result The value returned by the method
     */
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.annotation.PythonAfter)", returning = "result")
    public void executeSingleAfterReturningMethod(JoinPoint joinPoint, Object result) {
        annotationEvaluator.evaluate(joinPoint, PythonAfter.class, Map.of("result", result));
    }
}