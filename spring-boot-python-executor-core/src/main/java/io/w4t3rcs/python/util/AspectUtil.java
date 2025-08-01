package io.w4t3rcs.python.util;

import io.w4t3rcs.python.annotation.PythonParam;
import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class providing helper methods for aspect-oriented programming with Python integration.
 * This class contains methods for handling Python annotations on methods, extracting method
 * information from join points, and processing method parameters.
 */
@UtilityClass
public class AspectUtil {
    /**
     * Handles a Python annotation on a method by executing the associated Python script.
     * This method extracts the annotation from the method, gets the script and arguments,
     * and executes the script using the provided executor and resolvers.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @param pythonProcessor The processor responsible for resolving and running Python scripts
     * @param scriptGetter A function that retrieves the script path or content
     * @param argumentsGetter A function that extracts arguments from the join point
     */
    public void handlePythonAnnotation(JoinPoint joinPoint, PythonProcessor pythonProcessor, Supplier<String> scriptGetter, Function<JoinPoint, Map<String, Object>> argumentsGetter) {
        String script = scriptGetter.get();
        Map<String, Object> arguments = argumentsGetter.apply(joinPoint);
        pythonProcessor.process(script, null, arguments);
    }

    /**
     * Gets the Method object from a JoinPoint.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @return The Method object representing the intercepted method
     */
    public Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    /**
     * Extracts method parameters from a join point and creates a map of parameter names to values.
     * If a parameter is annotated with the {@link PythonParam}, the name from the annotation is used.
     * Otherwise, the parameter's actual name is used.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @return A map of parameter names to their values
     */
    public Map<String, Object> getPythonMethodParameters(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] objects = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = signature.getParameterNames();
        Map<String, Object> methodParameters = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(PythonParam.class)) {
                PythonParam annotation = parameter.getAnnotation(PythonParam.class);
                String value = annotation.value();
                methodParameters.put(value, objects[i]);
            } else {
                String parameterName = parameterNames[i];
                methodParameters.put(parameterName, objects[i]);
            }
        }
        return methodParameters;
    }
}
