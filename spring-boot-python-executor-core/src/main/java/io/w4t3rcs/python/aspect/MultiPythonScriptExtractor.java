package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.exception.AnnotationValueExtractingException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link PythonAnnotationValueExtractor} implementation extracting multiple Python scripts and their
 * associated active Spring profiles from a container annotation on a method.
 * <p>
 * This extractor assumes the container annotation declares a {@code value} attribute
 * that returns an array of annotations, each of which declares {@code value}, {@code script},
 * and {@code activeProfiles} attributes.
 * </p>
 * <p>
 * It uses reflection to read those attributes from the container annotation and its nested annotations
 * on the method represented by the {@link JoinPoint}.
 * </p>
 *
 * @see PythonAnnotationValueExtractor
 * @see SinglePythonScriptExtractor
 * @see PythonAnnotationValueCompounder
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class MultiPythonScriptExtractor implements PythonAnnotationValueExtractor {
    private final PythonMethodExtractor methodExtractor;

    /**
     * Extracts a map where each key is a Python script or file path specified by
     * the nested annotations' {@code value} or {@code script} attributes,
     * and each value is the array of active Spring profiles from their {@code activeProfiles} attributes.
     *
     * <p>If the nested annotation's {@code value} is blank, the {@code script} attribute is used instead.</p>
     *
     * @param joinPoint the join point representing the method invocation, must not be {@code null}
     * @param annotationClass the container annotation class to extract nested annotations from, must not be {@code null}
     * @param <A> the container annotation type
     * @return a map where keys are script strings and values are arrays of active profiles, never {@code null}, but may be empty if no nested annotations are present
     * @throws AnnotationValueExtractingException if annotation methods cannot be accessed or invoked, or the container annotation is missing on the method
     */
    @Override
    public <A extends Annotation> Map<String, String[]> getValue(JoinPoint joinPoint, Class<? extends A> annotationClass) {
        try {
            Method method = methodExtractor.getMethod(joinPoint);
            Method valueMethod = annotationClass.getMethod(VALUE_METHOD_NAME);
            A annotation = method.getDeclaredAnnotation(annotationClass);
            Annotation[] nestedAnnotations = (Annotation[]) valueMethod.invoke(annotation);
            Map<String, String[]> result = new HashMap<>();
            for (Annotation nestedAnnotation : nestedAnnotations) {
                Class<? extends Annotation> childClass = nestedAnnotation.getClass();
                Method childValueMethod = childClass.getMethod(VALUE_METHOD_NAME);
                Method childScriptMethod = childClass.getMethod(SCRIPT_METHOD_NAME);
                Method childActiveProfilesMethod = childClass.getMethod(ACTIVE_PROFILES_METHOD_NAME);
                String childValue = (String) childValueMethod.invoke(nestedAnnotation);
                String script = childValue.isBlank() ? (String) childScriptMethod.invoke(nestedAnnotation) : childValue;
                String[] activeProfiles = (String[]) childActiveProfilesMethod.invoke(nestedAnnotation);
                result.put(script, activeProfiles);
            }
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new AnnotationValueExtractingException(e);
        }
    }
}