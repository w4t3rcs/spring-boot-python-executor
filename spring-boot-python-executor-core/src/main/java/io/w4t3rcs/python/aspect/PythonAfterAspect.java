package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonAfter;
import io.w4t3rcs.python.annotation.PythonAfters;
import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Aspect that handles the execution of Python scripts through annotations.
 * This aspect intercepts methods annotated with {@link PythonAfters} and {@link PythonAfter}
 * annotations and executes the specified Python scripts after the method execution.
 */
@Aspect
@RequiredArgsConstructor
public class PythonAfterAspect extends AbstractPythonAspect {
    private final PythonProcessor pythonProcessor;
    private final Environment environment;

    /**
     * Executes Python scripts after methods annotated with {@link PythonAfters}.
     * This advice intercepts method calls and executes the Python scripts specified
     * in the annotation after the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @After("@annotation(io.w4t3rcs.python.annotation.PythonAfters)")
    public void executeMultipleAfterMethod(JoinPoint joinPoint) {
        Method method = this.getMethod(joinPoint);
        PythonAfter[] pythonAfters = method.getDeclaredAnnotation(PythonAfters.class).value();
        for (PythonAfter pythonAfter : pythonAfters) {
            String script = pythonAfter.value().isBlank() ? pythonAfter.script() : pythonAfter.value();
            this.handleProfiles(List.of(pythonAfter.activeProfiles()), environment,
                    () -> this.handleScript(joinPoint, pythonProcessor, () -> script, this::getMethodParameters));
        }
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
        Method method = this.getMethod(joinPoint);
        PythonAfter[] pythonAfters = method.getDeclaredAnnotation(PythonAfters.class).value();
        for (PythonAfter pythonAfter : pythonAfters) {
            String script = pythonAfter.value().isBlank() ? pythonAfter.script() : pythonAfter.value();
            this.handleProfiles(List.of(pythonAfter.activeProfiles()), environment,
                    () -> this.handleScript(joinPoint, pythonProcessor, () -> script, point -> {
                        Map<String, Object> arguments = this.getMethodParameters(point);
                        arguments.put("result", result);
                        return arguments;
                    }));
        }
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
        Method method = this.getMethod(joinPoint);
        PythonAfter pythonAfter = Objects.requireNonNull(AnnotatedElementUtils.findMergedAnnotation(method, PythonAfter.class));
        this.handleProfiles(List.of(pythonAfter.activeProfiles()), environment,
                () -> this.handleScript(joinPoint, pythonProcessor, pythonAfter::script, this::getMethodParameters));
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
        Method method = this.getMethod(joinPoint);
        PythonAfter pythonAfter = Objects.requireNonNull(AnnotatedElementUtils.findMergedAnnotation(method, PythonAfter.class));
        this.handleProfiles(List.of(pythonAfter.activeProfiles()), environment,
                () -> this.handleScript(joinPoint, pythonProcessor, pythonAfter::script, point -> {
                    Map<String, Object> arguments = this.getMethodParameters(point);
                    arguments.put("result", result);
                    return arguments;
                }));
    }
}