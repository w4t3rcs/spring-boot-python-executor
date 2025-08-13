package io.w4t3rcs.demo.service;

import io.w4t3rcs.demo.dto.DictScriptResponse;
import io.w4t3rcs.python.annotation.PythonAfter;
import io.w4t3rcs.python.annotation.PythonAfters;
import io.w4t3rcs.python.annotation.PythonBefore;
import io.w4t3rcs.python.annotation.PythonBefores;
import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimplePythonService {
    private static final String SIMPLE_SCRIPT = "simple_script.py";
    private static final String NUMERIC_SCRIPT = "numeric_script.py";
    private static final String DICT_SCRIPT = "dict_script.py";
    private final PythonProcessor pythonProcessor;

    @PythonBefores({
            @PythonBefore(SIMPLE_SCRIPT),
            @PythonBefore(NUMERIC_SCRIPT),
            @PythonBefore(DICT_SCRIPT),
    })
    public void doSomethingWithPythonBefore() {
        log.info("doSomethingWithPythonBefore()");
    }

    public void doSomethingWithPythonInside() {
        log.info("doSomethingWithPythonInside()");
        log.info("1 --> {}", pythonProcessor.process(SIMPLE_SCRIPT, String.class));
        log.info("2 --> {}", pythonProcessor.process(NUMERIC_SCRIPT, Float.class));
        log.info("3 --> {}", pythonProcessor.process(DICT_SCRIPT, DictScriptResponse.class));
    }

    @PythonAfters({
            @PythonAfter(SIMPLE_SCRIPT),
            @PythonAfter(NUMERIC_SCRIPT),
            @PythonAfter(DICT_SCRIPT),
    })
    public void doSomethingWithPythonAfter() {
        log.info("doSomethingWithPythonAfter()");
    }
}
