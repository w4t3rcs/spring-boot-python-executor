package io.w4t3rcs.python.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class AsyncPythonAnnotationEvaluator implements PythonAnnotationEvaluator {
    private final PythonAnnotationEvaluator annotationEvaluator;

    @Override
    public <A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass, Map<String, Object> additionalArguments) {
        CompletableFuture.runAsync(() -> annotationEvaluator.evaluate(joinPoint, annotationClass, additionalArguments));
    }
}
