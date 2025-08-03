package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonAfter;
import io.w4t3rcs.python.annotation.PythonBefore;
import io.w4t3rcs.python.annotation.PythonBefores;
import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * Aspect that handles the execution of Python scripts through annotations.
 * This aspect intercepts methods annotated with {@link PythonBefore} and {@link PythonAfter}
 * annotations and executes the specified Python scripts before or after the method execution.
 */
@Aspect
@RequiredArgsConstructor
public class PythonBeforeAspect extends AbstractPythonAspect {
    private final PythonProcessor pythonProcessor;
    private final Environment environment;

    /**
     * Executes Python scripts before methods annotated with {@link PythonBefores}.
     * This advice intercepts method calls and executes the Python scripts specified
     * in the annotation before the method execution.
     *
     * @param joinPoint The join point representing the intercepted method call
     */
    @Before("@annotation(io.w4t3rcs.python.annotation.PythonBefores)")
    public void executeMultipleBeforeMethod(JoinPoint joinPoint) {
        Method method = this.getMethod(joinPoint);
        PythonBefore[] pythonBefores = method.getDeclaredAnnotation(PythonBefores.class).value();
        for (PythonBefore pythonBefore : pythonBefores) {
            String script = pythonBefore.value().isBlank() ? pythonBefore.script() : pythonBefore.value();
            this.handleProfiles(List.of(pythonBefore.activeProfiles()), environment,
                    () -> this.handleScript(joinPoint, pythonProcessor, () -> script, this::getMethodParameters));
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
        Method method = this.getMethod(joinPoint);
        PythonBefore pythonBefore = Objects.requireNonNull(AnnotatedElementUtils.findMergedAnnotation(method, PythonBefore.class));
        this.handleProfiles(List.of(pythonBefore.activeProfiles()), environment,
                () -> this.handleScript(joinPoint, pythonProcessor, pythonBefore::script, this::getMethodParameters));
    }
}