package io.w4t3rcs.python.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Central autoconfiguration entry point for Python integration in Spring Boot applications.
 * <p>
 * This configuration class registers and imports all required infrastructure beans for executing Python scripts
 * from Java, including gRPC integration, local execution support, {@link py4j} bridge, annotation-based aspects,
 * file handling, and script resolution.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Automatic registration of Python execution infrastructure via Spring Boot autoconfiguration.</li>
 *   <li>Enables Aspect-Oriented Programming (AOP) support for executing Python scripts through annotations.</li>
 *   <li>Loads default configuration from {@code python-default.properties}.</li>
 *   <li>Aggregates multiple configuration modules into a single entry point.</li>
 * </ul>
 *
 * @see PythonAspectConfiguration
 * @see PythonExecutorConfiguration
 * @see PythonProcessorConfiguration
 * @see Py4JConfiguration
 * @see GrpcConfiguration
 * @see LocalConfiguration
 * @see PythonFileHandlerConfiguration
 * @see PythonResolverConfiguration
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
@EnableAspectJAutoProxy
@Import({
        GrpcConfiguration.class,
        LocalConfiguration.class,
        Py4JConfiguration.class,
        PythonFileHandlerConfiguration.class,
        PythonResolverConfiguration.class,
        PythonExecutorConfiguration.class,
        PythonProcessorConfiguration.class,
        PythonAspectConfiguration.class
})
@PropertySource("classpath:python-default.properties")
public class PythonAutoConfiguration {
}