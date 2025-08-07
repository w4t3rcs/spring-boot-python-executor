package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.processor.PythonProcessor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Interface for handling methods annotated with Python-related annotations.
 * <p>
 * This interface provides mechanisms to intercept method calls and execute associated Python scripts,
 * either synchronously or asynchronously, using a {@link PythonProcessor}.
 * <p>
 * Implementations of this interface are typically used in combination with AOP
 * to inject dynamic script execution behavior into annotated methods.
 */
public interface PythonAnnotationEvaluator {
    default <A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass) {
        evaluate(joinPoint, annotationClass, Map.of());
    }

    <A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass, Map<String, Object> additionalArguments);
}
