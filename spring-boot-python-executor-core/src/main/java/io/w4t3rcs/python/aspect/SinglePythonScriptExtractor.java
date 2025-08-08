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

/**
 * {@link PythonAnnotationValueExtractor} implementation extracting a single Python script value
 * and associated active Spring profiles from a method annotation.
 * <p>
 * This extractor assumes the target annotation declares {@code value} and {@code activeProfiles} attributes.
 * It uses reflection to read those attributes from the merged annotation instance found on the method represented by the {@link JoinPoint}.
 * </p>
 *
 * @see PythonAnnotationValueExtractor
 * @see MultiPythonScriptExtractor
 * @see PythonAnnotationValueCompounder
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class SinglePythonScriptExtractor implements PythonAnnotationValueExtractor {
    private final PythonMethodExtractor methodExtractor;

    /**
     * Extracts a map with a single entry, where the key is the Python script or file path specified
     * by the {@code value} attribute of the annotation, and the value is the array of active Spring profiles
     * from the {@code activeProfiles} attribute.
     *
     * @param joinPoint the join point representing the method invocation, must not be {@code null}
     * @param annotationClass the annotation class to extract values from, must not be {@code null}
     * @param <A> the annotation type
     * @return a map containing exactly one entry: script as key and active profiles as value, never {@code null}
     * @throws AnnotationValueExtractingException if annotation methods cannot be accessed or invoked, or the annotation is missing on the method
     */
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

