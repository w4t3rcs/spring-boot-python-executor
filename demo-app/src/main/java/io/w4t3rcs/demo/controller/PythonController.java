package io.w4t3rcs.demo.controller;

import io.w4t3rcs.demo.dto.MLScriptRequest;
import io.w4t3rcs.demo.dto.MLScriptResponse;
import io.w4t3rcs.demo.service.PythonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/python")
@RequiredArgsConstructor
public class PythonController {
    private final PythonService pythonService;

    @PostMapping("/before")
    public ResponseEntity<String> executeBefore(@RequestBody MLScriptRequest request) {
        pythonService.doSomethingWithPythonBefore(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/inside")
    public ResponseEntity<MLScriptResponse> executeInside(@RequestBody MLScriptRequest request) {
        return ResponseEntity.ok().body(pythonService.doSomethingWithPythonInside(request));
    }

    @PostMapping("/after")
    public ResponseEntity<MLScriptResponse> executeAfter(@RequestBody MLScriptRequest request) {
        pythonService.doSomethingWithPythonAfter(request);
        return ResponseEntity.ok().build();
    }
}
