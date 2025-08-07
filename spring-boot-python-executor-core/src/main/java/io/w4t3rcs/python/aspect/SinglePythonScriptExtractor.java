package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.exception.AnnotationValueExtractingException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class SinglePythonScriptExtractor implements PythonAnnotationValueExtractor {
    private final MethodExtractor methodExtractor;

    @Override
    public <A extends Annotation> Map<String, String[]> getValue(JoinPoint joinPoint, Class<? extends A> annotationClass) {
        try {
            Method method = methodExtractor.getMethod(joinPoint);
            Method valueMethod = annotationClass.getMethod(VALUE_METHOD_NAME);
            Method activeProfilesMethod = annotationClass.getMethod(ACTIVE_PROFILES_METHOD_NAME);
            A annotation = Objects.requireNonNull(AnnotatedElementUtils.findMergedAnnotation(method, annotationClass));
            return Map.of((String) valueMethod.invoke(annotation), (String[]) activeProfilesMethod.invoke(annotation));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new AnnotationValueExtractingException(e);
        }
    }
}
