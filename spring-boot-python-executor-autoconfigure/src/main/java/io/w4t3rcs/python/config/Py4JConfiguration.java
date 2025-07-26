package io.w4t3rcs.python.config;

import io.w4t3rcs.python.exception.GatewayServerCreationException;
import io.w4t3rcs.python.properties.Py4JProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
 * <p>The configuration is conditionally enabled based on the {@code spring.python.py4j.enabled} property,
 * which checks if Py4J integration is enabled or not.</p>
 */
@Configuration
@ConditionalOnProperty(name = "spring.python.py4j.enabled", havingValue = "true")
@EnableConfigurationProperties(Py4JProperties.class)
public class Py4JConfiguration {
    @Bean
    @ConditionalOnMissingBean(GatewayServer.class)
    public GatewayServer py4JGatewayServer(Py4JProperties py4JProperties) {
        if (py4JProperties.loggable()) GatewayServer.turnAllLoggingOn();
        try {
            CallbackClient pythonClient = new CallbackClient(py4JProperties.pythonPort(), InetAddress.getByName(py4JProperties.pythonHost()), py4JProperties.authToken());
            GatewayServer server = new GatewayServer.GatewayServerBuilder()
                    .entryPoint(null)
                    .javaPort(py4JProperties.port())
                    .javaAddress(InetAddress.getByName(py4JProperties.host()))
                    .callbackClient(pythonClient)
                    .connectTimeout(py4JProperties.connectTimeout())
                    .readTimeout(py4JProperties.readTimeout())
                    .serverSocketFactory(ServerSocketFactory.getDefault())
                    .customCommands(null)
                    .authToken(py4JProperties.authToken())
                    .build();
            server.start();
            return server;
        } catch (UnknownHostException e) {
            throw new GatewayServerCreationException(e);
        }
    }
}
