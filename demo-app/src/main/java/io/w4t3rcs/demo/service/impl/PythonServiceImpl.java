package io.w4t3rcs.demo.service.impl;

import io.w4t3rcs.demo.dto.DictScriptResponse;
import io.w4t3rcs.demo.dto.MLScriptRequest;
import io.w4t3rcs.demo.dto.MLScriptResponse;
import io.w4t3rcs.demo.service.PythonService;
import io.w4t3rcs.python.annotation.PythonAfter;
import io.w4t3rcs.python.annotation.PythonAfters;
import io.w4t3rcs.python.annotation.PythonBefore;
import io.w4t3rcs.python.annotation.PythonBefores;
import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PythonServiceImpl implements PythonService {
    private static final String SIMPLE_SCRIPT = "simple_script.py";
    private static final String NUMERIC_SCRIPT = "numeric_script.py";
    private static final String DICT_SCRIPT = "dict_script.py";
    private static final String ML_SCRIPT = "ml_script.py";
    private final PythonProcessor pythonProcessor;

    @Override
    @PythonBefores({
            @PythonBefore(SIMPLE_SCRIPT),
            @PythonBefore(NUMERIC_SCRIPT),
            @PythonBefore(DICT_SCRIPT),
            @PythonBefore(script = ML_SCRIPT, activeProfiles = "ml")
    })
    public void doSomethingWithPythonBefore(MLScriptRequest request) {
        log.info("doSomethingWithPythonBefore({})", request);
    }

    @Override
    public MLScriptResponse doSomethingWithPythonInside(MLScriptRequest request) {
        log.info("doSomethingWithPythonInside({})", request);
        Map<String, Object> pythonArguments = Map.of("request", request);
        log.info("1 --> {}", pythonProcessor.process(SIMPLE_SCRIPT, String.class));
        log.info("2 --> {}", pythonProcessor.process(NUMERIC_SCRIPT, Float.class));
        log.info("3 --> {}", pythonProcessor.process(DICT_SCRIPT, DictScriptResponse.class));
        MLScriptResponse response = pythonProcessor.process(ML_SCRIPT, MLScriptResponse.class, pythonArguments);
        log.info("4 --> {}", response);
        return response;
    }

    @Override
    @PythonAfters({
            @PythonAfter(SIMPLE_SCRIPT),
            @PythonAfter(NUMERIC_SCRIPT),
            @PythonAfter(DICT_SCRIPT),
            @PythonAfter(script = ML_SCRIPT, activeProfiles = "ml")
    })
    public void doSomethingWithPythonAfter(MLScriptRequest request) {
        log.info("doSomethingWithPythonAfter({})", request);
    }
}
