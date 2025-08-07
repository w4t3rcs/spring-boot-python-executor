package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.exception.AnnotationValueExtractingException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MultiPythonScriptExtractor implements PythonAnnotationValueExtractor {
    private final PythonMethodExtractor methodExtractor;

    @Override
    public <A extends Annotation> Map<String, String[]> getValue(JoinPoint joinPoint, Class<? extends A> annotationClass) {
        try {
            Method method = methodExtractor.getMethod(joinPoint);
            Method valueMethod = annotationClass.getMethod(VALUE_METHOD_NAME);
            A annotation = method.getDeclaredAnnotation(annotationClass);
            Annotation[] children = (Annotation[]) valueMethod.invoke(annotation);
            Map<String, String[]> result = new HashMap<>();
            for (Annotation child : children) {
                Class<? extends Annotation> childClass = child.getClass();
                Method childValueMethod = childClass.getMethod(VALUE_METHOD_NAME);
                Method childScriptMethod = childClass.getMethod(SCRIPT_METHOD_NAME);
                Method childActiveProfilesMethod = childClass.getMethod(ACTIVE_PROFILES_METHOD_NAME);
                String childValue = (String) childValueMethod.invoke(child);
                String script = childValue.isBlank() ? (String) childScriptMethod.invoke(child) : childValue;
                String[] activeProfiles = (String[]) childActiveProfilesMethod.invoke(child);
                result.put(script, activeProfiles);
            }
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new AnnotationValueExtractingException(e);
        }
    }
}
