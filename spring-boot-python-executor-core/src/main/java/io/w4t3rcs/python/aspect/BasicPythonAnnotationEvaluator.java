package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;

@RequiredArgsConstructor
public class BasicPythonAnnotationEvaluator implements PythonAnnotationEvaluator {
    private final ProfileChecker profileChecker;
    private final PythonAnnotationValueExtractorChain annotationValueExtractorChain;
    private final PythonArgumentsExtractor argumentsExtractor;
    private final PythonProcessor pythonProcessor;

    @Override
    public <A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass, Map<String, Object> additionalArguments) {
        Map<String, String[]> annotationValue = annotationValueExtractorChain.getValue(joinPoint, annotationClass);
        annotationValue.forEach((script, activeProfiles) -> {
            profileChecker.doOnProfiles(activeProfiles, () -> {
                Map<String, Object> arguments = argumentsExtractor.getArguments(joinPoint, additionalArguments);
                pythonProcessor.process(script, arguments);
            });
        });
    }
}
