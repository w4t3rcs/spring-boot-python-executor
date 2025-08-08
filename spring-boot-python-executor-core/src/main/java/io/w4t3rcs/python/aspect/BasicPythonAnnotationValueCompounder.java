package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.exception.AnnotationValueExtractingException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link PythonAnnotationValueCompounder} that aggregates values from multiple
 * {@link PythonAnnotationValueExtractor} instances.
 * <p>
 * This class sequentially invokes the {@link PythonAnnotationValueExtractor#getValue(JoinPoint, Class)} method on each
 * extractor in the provided list and merges the resulting maps into a single map.
 * In the case of key collisions, the last extracted value overwrites the previous one.
 * <p>
 * Exceptions thrown by individual extractors are ignored to allow partial failures without aborting the entire process.
 *
 * @see PythonAnnotationValueCompounder
 * @see PythonAnnotationValueExtractor
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class BasicPythonAnnotationValueCompounder implements PythonAnnotationValueCompounder {
    private final List<PythonAnnotationValueExtractor> annotationValueExtractors;

    /**
     * Merges the maps returned by each {@link PythonAnnotationValueExtractor} in {@link #annotationValueExtractors}.
     * <p>
     * Calls {@link PythonAnnotationValueExtractor#getValue(JoinPoint, Class)} on each extractor and puts all key-value pairs into a resulting map.
     * In the case of duplicate keys, the last value overwrites.
     * <p>
     * Exceptions thrown by individual extractors are ignored.
     * If the resulting map is empty after processing all extractors, throws {@link AnnotationValueExtractingException}.
     * <p>
     * The returned map is mutable and not thread-safe.
     *
     * @param joinPoint non-null join point from which to extract annotation values
     * @param annotationClass non-null annotation class to extract values for
     * @param <A> annotation type
     * @return a non-null, non-empty {@link Map} with keys of type {@link String} (Python scripts or script paths) and values of type {@code String[]} (active profiles)
     * @throws AnnotationValueExtractingException if no values could be extracted from any extractor
     */
    @Override
    public <A extends Annotation> Map<String, String[]> compound(JoinPoint joinPoint, Class<? extends A> annotationClass) {
        Map<String, String[]> mergedValue = new HashMap<>();
        for (PythonAnnotationValueExtractor annotationValueExtractor : annotationValueExtractors) {
            try {
                Map<String, String[]> value = annotationValueExtractor.getValue(joinPoint, annotationClass);
                mergedValue.putAll(value);
            } catch (Exception ignored) {
            }
        }
        if (mergedValue.isEmpty()) throw new AnnotationValueExtractingException();
        return mergedValue;
    }
}
