package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Interface that provides functions for extracting value from annotations
 * <p>
 * This interface is commonly used in aspects that execute Python scripts
 * and need access to method reflection and parameter mapping.
 */
public interface PythonAnnotationValueExtractor {
    String VALUE_METHOD_NAME = "value";
    String SCRIPT_METHOD_NAME = "script";
    String ACTIVE_PROFILES_METHOD_NAME = "activeProfiles";

    <A extends Annotation> Map<String, String[]> getValue(JoinPoint joinPoint, Class<? extends A> annotationClass);
}
