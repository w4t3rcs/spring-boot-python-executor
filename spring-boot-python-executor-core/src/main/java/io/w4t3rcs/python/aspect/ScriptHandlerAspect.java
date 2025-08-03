package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.processor.PythonProcessor;
import org.aspectj.lang.JoinPoint;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ScriptHandlerAspect {
    /**
     * Handles a method by executing the associated Python script.
     * This method gets the script and arguments,
     * and executes the script using the provided processor.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @param pythonProcessor The processor responsible for resolving and running Python scripts
     * @param scriptGetter A function that retrieves the script path or content
     * @param argumentsGetter A function that extracts arguments from the join point
     */
    void handleScript(JoinPoint joinPoint, PythonProcessor pythonProcessor, Supplier<String> scriptGetter, Function<JoinPoint, Map<String, Object>> argumentsGetter);
}
