package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.exception.AnnotationValueExtractingException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BasicPythonAnnotationValueExtractorChain implements PythonAnnotationValueExtractorChain {
    private final List<PythonAnnotationValueExtractor> annotationValueExtractors;

    @Override
    public <A extends Annotation> Map<String, String[]> getValue(JoinPoint joinPoint, Class<? extends A> annotationClass) {
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
