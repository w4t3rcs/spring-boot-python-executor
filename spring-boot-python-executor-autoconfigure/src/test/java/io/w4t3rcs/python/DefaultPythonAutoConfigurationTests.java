package io.w4t3rcs.python;

import io.w4t3rcs.python.config.PythonAutoConfiguration;
import io.w4t3rcs.python.executor.LocalPythonExecutor;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.BasicPythonFileHandler;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.processor.BasicPythonProcessor;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.resolver.BasicPythonResolverHolder;
import io.w4t3rcs.python.resolver.PythonResolverHolder;
import io.w4t3rcs.python.resolver.ResultResolver;
import io.w4t3rcs.python.resolver.SpelythonResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@AutoConfigureJson
@ContextConfiguration(classes = {PythonAutoConfiguration.class})
class DefaultPythonAutoConfigurationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testMandatoryBeansLoad() {
        Assertions.assertInstanceOf(BasicPythonFileHandler.class, applicationContext.getBean(PythonFileHandler.class));
        Assertions.assertInstanceOf(SpelythonResolver.class, applicationContext.getBean("spelythonResolver"));
        Assertions.assertInstanceOf(ResultResolver.class, applicationContext.getBean("resultResolver"));
        Assertions.assertInstanceOf(BasicPythonResolverHolder.class, applicationContext.getBean(PythonResolverHolder.class));
        Assertions.assertInstanceOf(LocalPythonExecutor.class, applicationContext.getBean(PythonExecutor.class));
        Assertions.assertInstanceOf(BasicPythonProcessor.class, applicationContext.getBean(PythonProcessor.class));
        Assertions.assertFalse(applicationContext.containsBean("gatewayServer"));
    }
}
