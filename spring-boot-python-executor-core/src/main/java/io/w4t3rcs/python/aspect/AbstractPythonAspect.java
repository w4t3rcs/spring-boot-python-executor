package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonParam;
import io.w4t3rcs.python.processor.PythonProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Abstract class providing helper methods for aspect-oriented programming with Python integration.
 * This class contains methods for handling Python annotations on methods, extracting method
 * information from join points, and processing method parameters.
 */
public abstract class AbstractPythonAspect implements ProfileHandlerAspect, ScriptHandlerAspect, MethodHandlerAspect  {
    @Override
    public void handleProfiles(List<String> profiles, Environment environment, Runnable action) {
        if (profiles == null || profiles.isEmpty()) action.run();
        else {
            String[] activeProfiles = environment.getActiveProfiles();
            for (String activeProfile : activeProfiles) {
                if (!profiles.contains(activeProfile)) return;
            }
            action.run();
        }
    }

    @Override
    public void handleScript(JoinPoint joinPoint, PythonProcessor pythonProcessor, Supplier<String> scriptGetter, Function<JoinPoint, Map<String, Object>> argumentsGetter) {
        Map<String, Object> arguments = argumentsGetter.apply(joinPoint);
        String script = scriptGetter.get();
        pythonProcessor.process(script, null, arguments);
    }

    @Override
    public Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    @Override
    public Map<String, Object> getMethodParameters(JoinPoint joinPoint) {
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
