package io.w4t3rcs.python;

import io.w4t3rcs.python.config.PythonCacheAutoConfiguration;
import io.w4t3rcs.python.executor.CachingPythonExecutor;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.CachingPythonFileHandler;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.processor.CachingPythonProcessor;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.resolver.CachingPythonResolverHolder;
import io.w4t3rcs.python.resolver.PythonResolverHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

@SpringBootTest
@AutoConfigureJson
@ContextConfiguration(classes = {CachingPythonLevelConfigurationTests.TestBeansConfiguration.class, PythonCacheAutoConfiguration.class})
@TestPropertySource(properties = "spring.python.cache.enabled=true")
class CachingPythonLevelConfigurationTests {
    @MockitoBean
    private CacheManager cacheManager;

    @Nested
    @TestPropertySource(properties = "spring.python.cache.levels=file")
    class OnlyFileTests {
        @Autowired
        private PythonFileHandler pythonFileHandler;
        @Autowired
        private List<PythonFileHandler> pythonFileHandlers;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(CachingPythonFileHandler.class, pythonFileHandler);
            Assertions.assertEquals(2, pythonFileHandlers.size());
        }
    }

    @Nested
    @TestPropertySource(properties = "spring.python.cache.levels=resolver")
    class OnlyResolverTests {
        @Autowired
        private PythonResolverHolder pythonResolverHolder;
        @Autowired
        private List<PythonResolverHolder> pythonResolverHolders;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(CachingPythonResolverHolder.class, pythonResolverHolder);
            Assertions.assertEquals(2, pythonResolverHolders.size());
        }
    }

    @Nested
    @TestPropertySource(properties = "spring.python.cache.levels=executor")
    class OnlyExecutorTests {
        @Autowired
        private PythonExecutor pythonExecutor;
        @Autowired
        private List<PythonExecutor> pythonExecutors;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(CachingPythonExecutor.class, pythonExecutor);
            Assertions.assertEquals(2, pythonExecutors.size());
        }
    }

    @Nested
    @TestPropertySource(properties = "spring.python.cache.levels=processor")
    class OnlyProcessorTests {
        @Autowired
        private PythonProcessor pythonProcessor;
        @Autowired
        private List<PythonProcessor> pythonProcessors;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(CachingPythonProcessor.class, pythonProcessor);
            Assertions.assertEquals(2, pythonProcessors.size());
        }
    }

    @Nested
    @TestPropertySource(properties = "spring.python.cache.levels=file, resolver, executor, processor")
    class MultipleLevelsTests {
        @Autowired
        private PythonFileHandler pythonFileHandler;
        @Autowired
        private PythonResolverHolder pythonResolverHolder;
        @Autowired
        private PythonExecutor pythonExecutor;
        @Autowired
        private PythonProcessor pythonProcessor;

        @Test
        void testMandatoryBeansLoad() {
            Assertions.assertInstanceOf(CachingPythonFileHandler.class, pythonFileHandler);
            Assertions.assertInstanceOf(CachingPythonResolverHolder.class, pythonResolverHolder);
            Assertions.assertInstanceOf(CachingPythonExecutor.class, pythonExecutor);
            Assertions.assertInstanceOf(CachingPythonProcessor.class, pythonProcessor);
        }
    }

    @TestConfiguration
    static class TestBeansConfiguration {
        @Bean
        public PythonFileHandler pythonFileHandler() {
            return Mockito.mock(PythonFileHandler.class);
        }

        @Bean
        public PythonResolverHolder pythonResolverHolder() {
            return Mockito.mock(PythonResolverHolder.class);
        }

        @Bean
        public PythonExecutor pythonExecutor() {
            return Mockito.mock(PythonExecutor.class);
        }

        @Bean
        public PythonProcessor pythonProcessor() {
            return Mockito.mock(PythonProcessor.class);
        }
    }
}
