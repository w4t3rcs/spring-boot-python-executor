package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Basic implementation of {@link PythonAnnotationEvaluator} that evaluates
 * Python script annotations.
 * <p>
 * This evaluator extracts Python scripts and their associated active profiles
 * from the annotation on the intercepted method, checks the active Spring profiles,
 * extracts method arguments, and processes the Python scripts accordingly.
 * </p>
 * <p>
 * The evaluation is performed synchronously in the calling thread.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * PythonAnnotationEvaluator evaluator = new BasicPythonAnnotationEvaluator(
 *     profileChecker,
 *     annotationValueExtractorChain,
 *     argumentsExtractor,
 *     pythonProcessor
 * );
 * evaluator.evaluate(joinPoint, PythonAfter.class);
 * }</pre>
 *
 * @see PythonAnnotationEvaluator
 * @see AsyncPythonAnnotationEvaluator
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class BasicPythonAnnotationEvaluator implements PythonAnnotationEvaluator {
    private final ProfileChecker profileChecker;
    private final PythonAnnotationValueCompounder annotationValueCompounder;
    private final PythonArgumentsExtractor argumentsExtractor;
    private final PythonProcessor pythonProcessor;

    /**
     * Evaluates the specified Python-related annotation.
     * <p>
     * For each Python script and its associated active profiles extracted from
     * the annotation on the method represented by {@code joinPoint}, this method:
     * <ul>
     *     <li>Checks if the current Spring profile matches the specified active profiles using {@link ProfileChecker}.</li>
     *     <li>If the profiles match, extracts method arguments using {@link PythonArgumentsExtractor}.</li>
     *     <li>Processes the Python script with the extracted arguments using {@link PythonProcessor}.</li>
     * </ul>
     * </p>
     * <p>
     * If no active profiles are specified for a script, the script is always executed.
     * </p>
     *
     * @param <A> the type of annotation to evaluate, must be a subtype of {@link Annotation}
     * @param joinPoint the AOP join point representing the intercepted method, must not be {@code null}
     * @param annotationClass the {@link Class} object of the annotation type to evaluate, must not be {@code null}
     * @param additionalArguments additional arguments to pass to the evaluator, must not be {@code null}
     */
    @Override
    public <A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass, Map<String, Object> additionalArguments) {
        Map<String, String[]> annotationValue = annotationValueCompounder.compound(joinPoint, annotationClass);
        annotationValue.forEach((script, activeProfiles) -> {
            profileChecker.doOnProfiles(activeProfiles, () -> {
                Map<String, Object> arguments = argumentsExtractor.getArguments(joinPoint, additionalArguments);
                pythonProcessor.process(script, arguments);
            });
        });
    }
}