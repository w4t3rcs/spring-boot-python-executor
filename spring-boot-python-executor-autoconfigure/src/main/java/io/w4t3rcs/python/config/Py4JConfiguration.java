package io.w4t3rcs.python.config;

import io.w4t3rcs.python.condition.Py4JCondition;
import io.w4t3rcs.python.exception.GatewayCreationException;
import io.w4t3rcs.python.properties.Py4JProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import py4j.CallbackClient;
import py4j.GatewayServer;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Configuration class for Py4J integration.
 * This class creates and starts a Py4J gateway server that enables
 * communication between Java and Python.
 * 
 * <p>The configuration is conditionally enabled based on the {@link Py4JCondition},
 * which checks if Py4J integration is enabled via properties or annotations.</p>
 */
@Configuration
@Conditional(Py4JCondition.class)
@EnableConfigurationProperties({Py4JProperties.class})
public class Py4JConfiguration {
    @Bean
    @ConditionalOnMissingBean(GatewayServer.class)
    public GatewayServer py4JGatewayServer(Py4JProperties py4JProperties) {
        if (py4JProperties.loggable()) GatewayServer.turnAllLoggingOn();
        try {
            GatewayServer gatewayServer = new GatewayServer(null,
                    py4JProperties.port(),
                    InetAddress.getByName(py4JProperties.host()),
                    py4JProperties.connectTimeout(),
                    py4JProperties.readTimeout(),
                    null,
                    new CallbackClient(py4JProperties.pythonPort(), InetAddress.getByName(py4JProperties.host())),
                    ServerSocketFactory.getDefault());
            gatewayServer.start();
            return gatewayServer;
        } catch (UnknownHostException e) {
            throw new GatewayCreationException(e);
        }
    }
}
