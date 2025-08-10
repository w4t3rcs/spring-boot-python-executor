package io.w4t3rcs.python.config;

import io.w4t3rcs.python.annotation.PythonAfter;
import io.w4t3rcs.python.annotation.PythonAfters;
import io.w4t3rcs.python.annotation.PythonBefore;
import io.w4t3rcs.python.annotation.PythonBefores;
import io.w4t3rcs.python.aspect.*;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.properties.PythonAspectProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Boot autoconfiguration for registering beans required to process
 * Python script annotations in aspects.
 *
 * <p>This configuration defines default implementations for core components such as:
 * <ul>
 *   <li>{@link ProfileChecker} — to validate active Spring profiles before Python script execution.</li>
 *   <li>{@link PythonMethodExtractor} and {@link PythonArgumentsExtractor} — to extract method.</li>
 *   <li>{@link PythonAnnotationValueExtractor} and {@link PythonAnnotationValueCompounder} — to parse and combine annotation values.</li>
 *   <li>{@link PythonAnnotationEvaluator} — to execute Python scripts synchronously or asynchronously.</li>
 *   <li>{@link PythonBeforeAspect} and {@link PythonAfterAspect} — to handle execution before and after method invocation.</li>
 * </ul>
 *
 *
 * @see PythonAspectProperties
 * @see PythonBeforeAspect
 * @see PythonAfterAspect
 * @see PythonBefore
 * @see PythonBefores
 * @see PythonAfter
 * @see PythonAfters
 * @see ProfileChecker
 * @see PythonMethodExtractor
 * @see PythonArgumentsExtractor
 * @see PythonAnnotationValueExtractor
 * @see PythonAnnotationValueCompounder
 * @see PythonAnnotationEvaluator
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(PythonAspectProperties.class)
public class PythonAspectConfiguration {
    /**
     * Creates a default {@link ProfileChecker} implementation.
     *
     * <p>This bean is created only if no other {@link ProfileChecker} bean is present in the context.
     * It uses the active Spring {@link Environment} profiles to determine whether Python scripts
     * should be executed for a given method.
     *
     * @param environment non-null Spring {@link Environment} used to resolve active profiles
     * @return non-null {@link BasicProfileChecker} instance
     */
    @Bean
    @ConditionalOnMissingBean(ProfileChecker.class)
    public ProfileChecker profileChecker(Environment environment) {
        return new BasicProfileChecker(environment);
    }

    /**
     * Creates a default {@link PythonMethodExtractor} implementation.
     *
     * <p>Responsible extracting methods from JoinPoint object.
     *
     * @return non-null {@link BasicPythonMethodExtractor} instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonMethodExtractor.class)
    public PythonMethodExtractor pythonMethodExtractor() {
        return new BasicPythonMethodExtractor();
    }

    /**
     * Creates a default {@link PythonArgumentsExtractor} implementation.
     *
     * <p>Extracts arguments from the method for passing to Python script evaluation.
     *
     * @param methodExtractor non-null {@link PythonMethodExtractor} used to locate and analyze methods
     * @return non-null {@link BasicPythonArgumentsExtractor} instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonArgumentsExtractor.class)
    public PythonArgumentsExtractor pythonArgumentsExtractor(PythonMethodExtractor methodExtractor) {
        return new BasicPythonArgumentsExtractor(methodExtractor);
    }

    /**
     * Creates an extractor for annotations that contain Python script as value().
     *
     * @param methodExtractor non-null {@link PythonMethodExtractor} for method analysis
     * @return non-null {@link SinglePythonScriptExtractor} instance
     */
    @Bean
    public PythonAnnotationValueExtractor singlePythonAnnotationValueExtractor(PythonMethodExtractor methodExtractor) {
        return new SinglePythonScriptExtractor(methodExtractor);
    }

    /**
     * Creates an extractor for annotations that contain an array of annotations as value() which contains Python scripts as value().
     *
     * @param methodExtractor non-null {@link PythonMethodExtractor} for method analysis
     * @return non-null {@link MultiPythonScriptExtractor} instance
     */
    @Bean
    public PythonAnnotationValueExtractor multiPythonAnnotationValueExtractor(PythonMethodExtractor methodExtractor) {
        return new MultiPythonScriptExtractor(methodExtractor);
    }

    /**
     * Creates a default {@link PythonAnnotationValueCompounder} implementation.
     *
     * <p>Combines results from multiple {@link PythonAnnotationValueExtractor} instances into a single value.
     *
     * @param annotationValueExtractors non-null, possibly empty list of extractors
     * @return non-null {@link BasicPythonAnnotationValueCompounder} instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonAnnotationValueCompounder.class)
    public PythonAnnotationValueCompounder pythonAnnotationValueCompounder(List<PythonAnnotationValueExtractor> annotationValueExtractors) {
        return new BasicPythonAnnotationValueCompounder(annotationValueExtractors);
    }

    /**
     * Creates the synchronous {@link PythonAnnotationEvaluator}.
     *
     * @param profileChecker non-null {@link ProfileChecker} to validate profile constraints
     * @param annotationValueExtractorChain non-null {@link PythonAnnotationValueCompounder} to combine annotation values
     * @param argumentsExtractor non-null {@link PythonArgumentsExtractor} to extract method arguments
     * @param pythonProcessor non-null {@link PythonProcessor} to execute Python code
     * @return non-null {@link BasicPythonAnnotationEvaluator} instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonAnnotationEvaluator.class)
    public PythonAnnotationEvaluator basicPythonAnnotationEvaluator(ProfileChecker profileChecker,
                                                                    PythonAnnotationValueCompounder annotationValueExtractorChain,
                                                                    PythonArgumentsExtractor argumentsExtractor,
                                                                    PythonProcessor pythonProcessor) {
        return new BasicPythonAnnotationEvaluator(profileChecker, annotationValueExtractorChain, argumentsExtractor, pythonProcessor);
    }

    /**
     * Creates an asynchronous wrapper for an existing {@link PythonAnnotationEvaluator}.
     *
     * @param annotationEvaluator non-null existing evaluator
     * @return non-null {@link AsyncPythonAnnotationEvaluator} instance
     */
    @Bean
    @ConditionalOnBean(PythonAnnotationEvaluator.class)
    public PythonAnnotationEvaluator asyncPythonAnnotationEvaluator(PythonAnnotationEvaluator annotationEvaluator) {
        return new AsyncPythonAnnotationEvaluator(annotationEvaluator);
    }

    /**
     * Creates the {@link PythonBeforeAspect} for handling {@code BEFORE} scope executions.
     *
     * @param aspectProperties non-null configuration properties
     * @param basicPythonAnnotationEvaluator non-null synchronous evaluator
     * @param asyncPythonAnnotationEvaluator non-null asynchronous evaluator
     * @return non-null {@link PythonBeforeAspect} instance
     */
    @Bean
    public PythonBeforeAspect pythonBeforeAspect(PythonAspectProperties aspectProperties,
                                                 @Qualifier("basicPythonAnnotationEvaluator") PythonAnnotationEvaluator basicPythonAnnotationEvaluator,
                                                 @Qualifier("asyncPythonAnnotationEvaluator") PythonAnnotationEvaluator asyncPythonAnnotationEvaluator) {
        return new PythonBeforeAspect(this.isAsync(aspectProperties, PythonAspectProperties.AsyncScope.BEFORE) ? asyncPythonAnnotationEvaluator : basicPythonAnnotationEvaluator);
    }

    /**
     * Creates the {@link PythonAfterAspect} for handling {@code AFTER} scope executions.
     *
     * @param aspectProperties non-null configuration properties
     * @param basicPythonAnnotationEvaluator non-null synchronous evaluator
     * @param asyncPythonAnnotationEvaluator non-null asynchronous evaluator
     * @return non-null {@link PythonAfterAspect} instance
     */
    @Bean
    public PythonAfterAspect pythonAfterAspect(PythonAspectProperties aspectProperties,
                                               @Qualifier("basicPythonAnnotationEvaluator") PythonAnnotationEvaluator basicPythonAnnotationEvaluator,
                                               @Qualifier("asyncPythonAnnotationEvaluator") PythonAnnotationEvaluator asyncPythonAnnotationEvaluator) {
        return new PythonAfterAspect(this.isAsync(aspectProperties, PythonAspectProperties.AsyncScope.AFTER) ? asyncPythonAnnotationEvaluator : basicPythonAnnotationEvaluator);
    }

    /**
     * Checks whether a given {@link PythonAspectProperties.AsyncScope} is configured for asynchronous execution.
     *
     * @param aspectProperties non-null configuration properties
     * @param scope non-null scope to check
     * @return {@code true} if asynchronous execution is enabled for the given scope, {@code false} otherwise
     */
    private boolean isAsync(PythonAspectProperties aspectProperties, PythonAspectProperties.AsyncScope scope) {
        return Arrays.asList(aspectProperties.asyncScopes()).contains(scope);
    }
}