package io.w4t3rcs.python;

import io.w4t3rcs.python.aspect.*;
import io.w4t3rcs.python.config.PythonAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@AutoConfigureJson
@ContextConfiguration(classes = {PythonAutoConfiguration.class})
class PythonAspectConfigurationTests {
    @Autowired
    private ProfileChecker profileChecker;
    @Autowired
    private PythonMethodExtractor pythonMethodExtractor;
    @Autowired
    private PythonArgumentsExtractor pythonArgumentsExtractor;
    @Autowired
    @Qualifier("singlePythonAnnotationValueExtractor")
    private PythonAnnotationValueExtractor singlePythonAnnotationValueExtractor;
    @Autowired
    @Qualifier("multiPythonAnnotationValueExtractor")
    private PythonAnnotationValueExtractor multiPythonAnnotationValueExtractor;
    @Autowired
    private PythonAnnotationValueCompounder pythonAnnotationValueCompounder;
    @Autowired
    @Qualifier("basicPythonAnnotationEvaluator")
    private PythonAnnotationEvaluator basicPythonAnnotationEvaluator;
    @Autowired
    @Qualifier("asyncPythonAnnotationEvaluator")
    private PythonAnnotationEvaluator asyncPythonAnnotationEvaluator;
    @Autowired
    private PythonBeforeAspect pythonBeforeAspect;
    @Autowired
    private PythonAfterAspect pythonAfterAspect;

    @Test
    void testMandatoryBeansLoad() {
        Assertions.assertInstanceOf(BasicProfileChecker.class, profileChecker);
        Assertions.assertInstanceOf(BasicPythonMethodExtractor.class, pythonMethodExtractor);
        Assertions.assertInstanceOf(BasicPythonArgumentsExtractor.class, pythonArgumentsExtractor);
        Assertions.assertInstanceOf(SinglePythonScriptExtractor.class, singlePythonAnnotationValueExtractor);
        Assertions.assertInstanceOf(MultiPythonScriptExtractor.class, multiPythonAnnotationValueExtractor);
        Assertions.assertInstanceOf(BasicPythonAnnotationValueCompounder.class, pythonAnnotationValueCompounder);
        Assertions.assertInstanceOf(BasicPythonAnnotationEvaluator.class, basicPythonAnnotationEvaluator);
        Assertions.assertInstanceOf(AsyncPythonAnnotationEvaluator.class, asyncPythonAnnotationEvaluator);
        Assertions.assertNotNull(pythonBeforeAspect);
        Assertions.assertNotNull(pythonAfterAspect);
    }
}
