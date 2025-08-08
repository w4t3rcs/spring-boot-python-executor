package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Interface for combining or aggregating Python script values and their active profiles
 * extracted from annotations on methods represented by a {@link JoinPoint}.
 *
 * <p>Implementations typically merge results from multiple {@link PythonAnnotationValueExtractor}
 * instances or different annotation sources into a single map.</p>
 *
 * @see BasicPythonAnnotationValueCompounder
 * @see PythonAnnotationValueExtractor
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface PythonAnnotationValueCompounder {
    /**
     * Aggregates and combines Python script strings or file paths along with their associated
     * active Spring profiles extracted from the specified annotation on the method identified
     * by the given join point.
     *
     * @param joinPoint the join point representing the method invocation, must not be {@code null}
     * @param annotationClass the annotation class to extract and combine values from, must not be {@code null}
     * @param <A> the type of the annotation
     * @return a combined map of script strings to arrays of active profiles, never {@code null}
     */
    <A extends Annotation> Map<String, String[]> compound(JoinPoint joinPoint, Class<? extends A> annotationClass);
}