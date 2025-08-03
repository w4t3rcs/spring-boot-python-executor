package io.w4t3rcs.python.config;

import io.w4t3rcs.python.aspect.PythonAfterAspect;
import io.w4t3rcs.python.aspect.PythonBeforeAspect;
import io.w4t3rcs.python.processor.PythonProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PythonAspectConfiguration {
    @Bean
    public PythonBeforeAspect pythonBeforeAspect(PythonProcessor pythonProcessor, Environment environment) {
        return new PythonBeforeAspect(pythonProcessor, environment);
    }

    @Bean
    public PythonAfterAspect pythonAfterAspect(PythonProcessor pythonProcessor, Environment environment) {
        return new PythonAfterAspect(pythonProcessor, environment);
    }
}
