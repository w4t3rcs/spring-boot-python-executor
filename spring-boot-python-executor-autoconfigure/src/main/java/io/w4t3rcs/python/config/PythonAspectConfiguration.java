package io.w4t3rcs.python.config;

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

@Configuration
@EnableConfigurationProperties(PythonAspectProperties.class)
public class PythonAspectConfiguration {
    @Bean
    @ConditionalOnMissingBean(ProfileChecker.class)
    public ProfileChecker profileChecker(Environment environment) {
        return new BasicProfileChecker(environment);
    }

    @Bean
    @ConditionalOnMissingBean(MethodExtractor.class)
    public MethodExtractor methodExtractor() {
        return new PythonMethodExtractor();
    }

    @Bean
    public PythonArgumentsExtractor pythonArgumentsExtractor(MethodExtractor methodExtractor) {
        return new BasicPythonArgumentsExtractor(methodExtractor);
    }

    @Bean
    public PythonAnnotationValueExtractor singlePythonAnnotationValueExtractor(MethodExtractor methodExtractor) {
        return new SinglePythonScriptExtractor(methodExtractor);
    }

    @Bean
    public PythonAnnotationValueExtractor multiPythonAnnotationValueExtractor(MethodExtractor methodExtractor) {
        return new MultiPythonScriptExtractor(methodExtractor);
    }

    @Bean
    public PythonAnnotationValueExtractorChain pythonAnnotationValueExtractorChain(List<PythonAnnotationValueExtractor> annotationValueExtractors) {
        return new BasicPythonAnnotationValueExtractorChain(annotationValueExtractors);
    }

    @Bean
    @ConditionalOnMissingBean(PythonAnnotationEvaluator.class)
    public PythonAnnotationEvaluator basicPythonAnnotationEvaluator(ProfileChecker profileChecker,
                                                                    PythonAnnotationValueExtractorChain annotationValueExtractorChain,
                                                                    PythonArgumentsExtractor argumentsExtractor,
                                                                    PythonProcessor pythonProcessor) {
        return new BasicPythonAnnotationEvaluator(profileChecker, annotationValueExtractorChain, argumentsExtractor, pythonProcessor);
    }

    @Bean
    @ConditionalOnBean(PythonAnnotationEvaluator.class)
    public PythonAnnotationEvaluator asyncPythonAnnotationEvaluator(PythonAnnotationEvaluator annotationEvaluator) {
        return new AsyncPythonAnnotationEvaluator(annotationEvaluator);
    }

    @Bean
    public PythonBeforeAspect pythonBeforeAspect(PythonAspectProperties aspectProperties,
                                                 @Qualifier("basicPythonAnnotationEvaluator") PythonAnnotationEvaluator basicPythonAnnotationEvaluator,
                                                 @Qualifier("asyncPythonAnnotationEvaluator") PythonAnnotationEvaluator asyncPythonAnnotationEvaluator) {
        if (this.isAsync(aspectProperties, PythonAspectProperties.AsyncScope.BEFORE)) {
            return new PythonBeforeAspect(asyncPythonAnnotationEvaluator);
        } else {
            return new PythonBeforeAspect(basicPythonAnnotationEvaluator);
        }
    }

    @Bean
    public PythonAfterAspect pythonAfterAspect(PythonAspectProperties aspectProperties,
                                               @Qualifier("basicPythonAnnotationEvaluator") PythonAnnotationEvaluator basicPythonAnnotationEvaluator,
                                               @Qualifier("asyncPythonAnnotationEvaluator") PythonAnnotationEvaluator asyncPythonAnnotationEvaluator) {
        if (this.isAsync(aspectProperties, PythonAspectProperties.AsyncScope.AFTER)) {
            return new PythonAfterAspect(asyncPythonAnnotationEvaluator);
        } else {
            return new PythonAfterAspect(basicPythonAnnotationEvaluator);
        }
    }

    private boolean isAsync(PythonAspectProperties aspectProperties, PythonAspectProperties.AsyncScope scope) {
        return Arrays.asList(aspectProperties.asyncScopes()).contains(scope);
    }
}
