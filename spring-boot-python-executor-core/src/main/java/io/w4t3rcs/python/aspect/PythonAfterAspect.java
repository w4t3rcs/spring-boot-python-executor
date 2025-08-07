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
 * Aspect that handles the execution of Python scripts through annotations.
 * This aspect intercepts methods annotated with {@link PythonAfters} and {@link PythonAfter}
 * annotations and executes the specified Python scripts after the method execution.
 */
@Aspect
@RequiredArgsConstructor
public class PythonAfterAspect {
    private final PythonAnnotationEvaluator annotationEvaluator;

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfters} return a result.
     * This advice intercepts method calls and executes the Python scripts specified
     * in the annotation after the method returns.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @param result The value returned by the method
     */
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.annotation.PythonAfters)", returning = "result")
    public void executeMultipleAfterMethod(JoinPoint joinPoint, Object result) {
        this.evaluateWithResult(joinPoint, result, PythonAfters.class);
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
    public void executeSingleAfterMethod(JoinPoint joinPoint, Object result) {
        this.evaluateWithResult(joinPoint, result, PythonAfter.class);
    }

    /**
     * Executes Python scripts after methods annotated with return a result.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @param result The value returned by the method
     * @param annotation The annotation class
     */
    private void evaluateWithResult(JoinPoint joinPoint, Object result, Class<? extends Annotation> annotation) {
        HashMap<String, Object> additionalArguments = new HashMap<>();
        if (result != null) additionalArguments.put("result", result);
        annotationEvaluator.evaluate(joinPoint, annotation, additionalArguments);
    }
}