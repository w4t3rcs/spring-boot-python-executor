package io.w4t3rcs.demo.service;

import io.w4t3rcs.demo.dto.MLScriptRequest;
import io.w4t3rcs.demo.dto.MLScriptResponse;
import io.w4t3rcs.python.dto.PythonExecutionResponse;
import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MLPythonService {
    private static final String ML1_SCRIPT = "ml1_script.py";
    private static final String ML2_SCRIPT = "ml2_script.py";
    private final PythonProcessor pythonProcessor;

    public MLScriptResponse processML1Script(MLScriptRequest mlScriptRequest) {
        Map<String, Object> arguments = Map.of("request", mlScriptRequest);
        PythonExecutionResponse<MLScriptResponse> response = pythonProcessor.process(ML1_SCRIPT, MLScriptResponse.class, arguments);
        return response.body();
    }

    public double processML2Script(String text) {
        Map<String, Object> arguments = Map.of("text", text);
        PythonExecutionResponse<Double> response = pythonProcessor.process(ML2_SCRIPT, Double.class, arguments);
        return response.body();
    }
}
