package io.w4t3rcs.demo.service;

import io.w4t3rcs.demo.dto.MLScriptRequest;
import io.w4t3rcs.demo.dto.MLScriptResponse;

public interface PythonService {
    void doSomethingWithPythonBefore(MLScriptRequest request);

    MLScriptResponse doSomethingWithPythonInside(MLScriptRequest request);

    void doSomethingWithPythonAfter(MLScriptRequest request);
}
