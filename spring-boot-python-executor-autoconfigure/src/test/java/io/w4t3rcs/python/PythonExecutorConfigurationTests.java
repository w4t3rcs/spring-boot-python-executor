package io.w4t3rcs.python;

import io.w4t3rcs.python.config.PythonAutoConfiguration;
import io.w4t3rcs.python.executor.GrpcPythonExecutor;
import io.w4t3rcs.python.executor.LocalPythonExecutor;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.executor.RestPythonExecutor;
import io.w4t3rcs.python.local.*;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureJson
@ContextConfiguration(classes = {PythonAutoConfiguration.class})
class PythonExecutorConfigurationTests {
    @MockitoBean
    private PythonServiceGrpc.PythonServiceBlockingStub stub;

    @Nested
    @TestPropertySource(properties = "spring.python.executor.type=local")
    class LocalTests {
        @Autowired
        private PythonExecutor pythonExecutor;
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(LocalPythonExecutor.class, pythonExecutor);
            Assertions.assertFalse(applicationContext.containsBean("restPythonExecutor"));
            Assertions.assertFalse(applicationContext.containsBean("grpcPythonExecutor"));
            Assertions.assertInstanceOf(ProcessStarterImpl.class, applicationContext.getBean(ProcessStarter.class));
            Assertions.assertInstanceOf(InputProcessHandler.class, applicationContext.getBean("inputProcessHandler"));
            Assertions.assertInstanceOf(ErrorProcessHandler.class, applicationContext.getBean("errorProcessHandler"));
            Assertions.assertInstanceOf(ProcessFinisherImpl.class, applicationContext.getBean(ProcessFinisher.class));
        }
    }

    @Nested
    @TestPropertySource(properties = "spring.python.executor.type=rest")
    class RestTests {
        @Autowired
        private PythonExecutor pythonExecutor;
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(RestPythonExecutor.class, pythonExecutor);
            Assertions.assertFalse(applicationContext.containsBean("localPythonExecutor"));
            Assertions.assertFalse(applicationContext.containsBean("grpcPythonExecutor"));
        }
    }

    @Nested
    @TestPropertySource(properties = "spring.python.executor.type=grpc")
    class GrpcTests {
        @Autowired
        private PythonExecutor pythonExecutor;
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(GrpcPythonExecutor.class, pythonExecutor);
            Assertions.assertFalse(applicationContext.containsBean("restPythonExecutor"));
            Assertions.assertFalse(applicationContext.containsBean("localPythonExecutor"));
        }
    }
}
