package io.w4t3rcs.python;

import io.w4t3rcs.python.aspect.PythonAfterAspect;
import io.w4t3rcs.python.aspect.PythonBeforeAspect;
import io.w4t3rcs.python.config.PythonAutoConfiguration;
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
class PythonAspectConfigurationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testMandatoryBeansLoad() {
        Assertions.assertInstanceOf(PythonBeforeAspect.class, applicationContext.getBean(PythonBeforeAspect.class));
        Assertions.assertInstanceOf(PythonAfterAspect.class, applicationContext.getBean(PythonAfterAspect.class));
    }
}
