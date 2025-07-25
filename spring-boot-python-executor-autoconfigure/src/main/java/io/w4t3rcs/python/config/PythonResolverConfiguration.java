package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.condition.*;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.resolver.impl.Py4JResolver;
import io.w4t3rcs.python.resolver.impl.RestrictedPythonResolver;
import io.w4t3rcs.python.resolver.impl.ResultResolver;
import io.w4t3rcs.python.resolver.impl.SpelythonResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class for {@link PythonResolver}.
 * This class sets up the core infrastructure for {@link PythonResolver} bean declaration.
 */
@Configuration
@EnableConfigurationProperties(PythonResolverProperties.class)
public class PythonResolverConfig {
    @Bean
    @Conditional(SpelythonResolverCondition.class)
    public PythonResolver spelythonResolver(PythonResolverProperties resolverProperties, ApplicationContext applicationContext, ObjectMapper objectMapper) {
        return new SpelythonResolver(resolverProperties, applicationContext, objectMapper);
    }

    @Bean
    @Conditional({Py4JCondition.class, Py4JResolverCondition.class})
    public PythonResolver py4JResolver(PythonResolverProperties resolverProperties) {
        return new Py4JResolver(resolverProperties);
    }

    @Bean
    @Conditional(RestrictedPythonResolverCondition.class)
    public PythonResolver restrictedPythonResolver(PythonResolverProperties resolverProperties) {
        return new RestrictedPythonResolver(resolverProperties);
    }

    @Bean
    @Conditional(ResultResolverCondition.class)
    public PythonResolver resultResolver(PythonResolverProperties resolverProperties) {
        return new ResultResolver(resolverProperties);
    }
}