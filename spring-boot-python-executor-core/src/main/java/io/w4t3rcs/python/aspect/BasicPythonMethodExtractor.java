package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class BasicPythonMethodExtractor implements PythonMethodExtractor {
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
