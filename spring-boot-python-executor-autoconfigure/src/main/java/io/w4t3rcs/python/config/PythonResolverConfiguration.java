package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.condition.Py4JResolverCondition;
import io.w4t3rcs.python.condition.RestrictedPythonResolverCondition;
import io.w4t3rcs.python.condition.ResultResolverCondition;
import io.w4t3rcs.python.condition.SpelythonResolverCondition;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import io.w4t3rcs.python.resolver.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * Main configuration class for {@link PythonResolver}.
 * This class sets up the core infrastructure for {@link PythonResolver} bean declaration.
 */
@Configuration
@EnableConfigurationProperties(PythonResolverProperties.class)
public class PythonResolverConfiguration {
    @Bean
    @Order(1)
    @Conditional(SpelythonResolverCondition.class)
    public PythonResolver spelythonResolver(PythonResolverProperties resolverProperties, ApplicationContext applicationContext, ObjectMapper objectMapper) {
        return new SpelythonResolver(resolverProperties, applicationContext, objectMapper);
    }

    @Bean
    @Order(2)
    @Conditional(Py4JResolverCondition.class)
    @ConditionalOnProperty(name = "spring.python.py4j.enabled", havingValue = "true")
    public PythonResolver py4JResolver(PythonResolverProperties resolverProperties) {
        return new Py4JResolver(resolverProperties);
    }

    @Bean
    @Order(3)
    @Conditional(RestrictedPythonResolverCondition.class)
    public PythonResolver restrictedPythonResolver(PythonResolverProperties resolverProperties) {
        return new RestrictedPythonResolver(resolverProperties);
    }

    @Bean
    @Order(4)
    @Conditional(ResultResolverCondition.class)
    public PythonResolver resultResolver(PythonResolverProperties resolverProperties) {
        return new ResultResolver(resolverProperties);
    }

    @Bean
    @ConditionalOnMissingBean(PythonResolverHolder.class)
    public PythonResolverHolder basicPythonResolverHolder(List<PythonResolver> pythonResolvers) {
        return new BasicPythonResolverHolder(pythonResolvers);
    }
}