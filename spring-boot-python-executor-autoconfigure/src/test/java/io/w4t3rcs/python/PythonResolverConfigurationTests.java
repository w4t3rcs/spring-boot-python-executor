package io.w4t3rcs.python;

import io.w4t3rcs.python.config.PythonAutoConfiguration;
import io.w4t3rcs.python.resolver.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@AutoConfigureJson
@ContextConfiguration(classes = {PythonAutoConfiguration.class})
class PythonResolverConfigurationTests {
    @Nested
    @TestPropertySource(properties = "spring.python.resolver.declared=")
    class NoneTests {
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(BasicPythonResolverHolder.class, applicationContext.getBean(PythonResolverHolder.class));
            Assertions.assertThrows(BeansException.class, () -> applicationContext.getBean(PythonResolver.class));
        }
    }

    @Nested
    @TestPropertySource(properties = "spring.python.resolver.declared=spelython")
    class SingleTests {
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(BasicPythonResolverHolder.class, applicationContext.getBean(PythonResolverHolder.class));
            Assertions.assertDoesNotThrow(() -> applicationContext.getBeanProvider(PythonResolver.class).getIfUnique());
            Assertions.assertEquals(applicationContext.getBean(PythonResolverHolder.class)
                    .getResolvers(), applicationContext.getBeanProvider(PythonResolver.class)
                    .stream()
                    .toList());
        }
    }

    @Nested
    @TestPropertySource(properties = "spring.python.resolver.declared=spelython, restricted_python, result")
    class MultipleTests {
        @Autowired
        private ApplicationContext applicationContext;
        @Autowired
        private List<PythonResolver> pythonResolvers;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(SpelythonResolver.class, pythonResolvers.get(0));
            Assertions.assertInstanceOf(RestrictedPythonResolver.class, pythonResolvers.get(1));
            Assertions.assertInstanceOf(ResultResolver.class, pythonResolvers.get(2));
            Assertions.assertEquals(applicationContext.getBean(PythonResolverHolder.class)
                    .getResolvers(), pythonResolvers);
        }
    }
}
