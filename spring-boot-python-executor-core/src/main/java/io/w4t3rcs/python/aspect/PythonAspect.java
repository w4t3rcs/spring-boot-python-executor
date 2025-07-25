package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.metadata.PythonAfter;
import io.w4t3rcs.python.metadata.PythonBefore;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.util.AspectUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Aspect that handles the execution of Python scripts through annotations.
 * This aspect intercepts methods annotated with {@link PythonBefore} and {@link PythonAfter}
 * annotations and executes the specified Python scripts before or after the method execution.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PythonAspect {
    private final PythonProcessor pythonProcessor;

    /**
     * Executes Python scripts before methods annotated with {@link PythonAspect}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation before the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.metadata.PythonBefore)")
    public void executeBeforeMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, pythonProcessor, PythonBefore.class, PythonBefore::value,
                AspectUtil::getMethodParameters);
    }

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfter}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation after the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @After("@annotation(io.w4t3rcs.python.metadata.PythonAfter)")
    public void executeAfterMethod(JoinPoint joinPoint) {
        AspectUtil.handlePythonAnnotation(joinPoint, pythonProcessor, PythonAfter.class, PythonAfter::value,
                AspectUtil::getMethodParameters);
    }

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfter} return a result.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation after the method returns.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @param result The value returned by the method
     */
    @AfterReturning(pointcut = "@annotation(io.w4t3rcs.python.metadata.PythonAfter)", returning = "result")
    public void executeAfterReturningMethod(JoinPoint joinPoint, Object result) {
        AspectUtil.handlePythonAnnotation(joinPoint, pythonProcessor, PythonAfter.class, PythonAfter::value,
                point -> {
            Map<String, Object> arguments = AspectUtil.getMethodParameters(point);
            arguments.put("result", result);
            return arguments;
        });
    }
}