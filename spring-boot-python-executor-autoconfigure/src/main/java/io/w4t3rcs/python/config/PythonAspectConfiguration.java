package io.w4t3rcs.python.config;

import io.w4t3rcs.python.aspect.PythonAfterAspect;
import io.w4t3rcs.python.aspect.PythonBeforeAspect;
import io.w4t3rcs.python.processor.PythonProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PythonAspectConfiguration {
    @Bean
    public PythonBeforeAspect pythonBeforeAspect(PythonProcessor pythonProcessor) {
        return new PythonBeforeAspect(pythonProcessor);
    }

    @Bean
    public PythonAfterAspect pythonAfterAspect(PythonProcessor pythonProcessor) {
        return new PythonAfterAspect(pythonProcessor);
    }
}
