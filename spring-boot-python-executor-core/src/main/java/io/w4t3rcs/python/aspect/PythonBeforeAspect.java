package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonAfter;
import io.w4t3rcs.python.annotation.PythonBefore;
import io.w4t3rcs.python.annotation.PythonBefores;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.util.AspectUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.lang.reflect.Method;

/**
 * Aspect that handles the execution of Python scripts through annotations.
 * This aspect intercepts methods annotated with {@link PythonBefore} and {@link PythonAfter}
 * annotations and executes the specified Python scripts before or after the method execution.
 */
@Aspect
@RequiredArgsConstructor
public class PythonBeforeAspect {
    private final PythonProcessor pythonProcessor;

    /**
     * Executes Python scripts before methods annotated with {@link PythonBefores}.
     * This advice intercepts method calls and executes the Python scripts specified
     * in the annotation before the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.annotation.PythonBefore)")
    public void executeMultipleBeforeMethod(JoinPoint joinPoint) {
        Method method = AspectUtil.getMethod(joinPoint);
        PythonBefores pythonBefores = method.getDeclaredAnnotation(PythonBefores.class);
        for (PythonBefore pythonBefore : pythonBefores.value()) {
            AspectUtil.handlePythonAnnotation(joinPoint, pythonProcessor,
                    pythonBefore::value,
                    AspectUtil::getPythonMethodParameters);
        }
    }

    /**
     * Executes Python scripts before methods annotated with {@link PythonBefore}.
     * This advice intercepts method calls and executes the Python script specified
     * in the annotation before the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.annotation.PythonBefore)")
    public void executeSingleBeforeMethod(JoinPoint joinPoint) {
        Method method = AspectUtil.getMethod(joinPoint);
        AspectUtil.handlePythonAnnotation(joinPoint, pythonProcessor,
                () -> method.getDeclaredAnnotation(PythonBefore.class).value(),
                AspectUtil::getPythonMethodParameters);
    }
}