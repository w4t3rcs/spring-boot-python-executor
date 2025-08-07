package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface PythonAnnotationValueExtractorChain {
    <A extends Annotation> Map<String, String[]> getValue(JoinPoint joinPoint, Class<? extends A> annotationClass);
}
