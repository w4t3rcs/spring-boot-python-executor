package io.w4t3rcs.demo.controller;

import io.w4t3rcs.demo.dto.CalculatorRequest;
import io.w4t3rcs.demo.dto.MLScriptRequest;
import io.w4t3rcs.demo.dto.MLScriptResponse;
import io.w4t3rcs.demo.service.MLPythonService;
import io.w4t3rcs.demo.service.PriceCalculatorPythonService;
import io.w4t3rcs.demo.service.SimplePythonService;
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
    private final SimplePythonService simplePythonService;
    private final PriceCalculatorPythonService priceCalculatorPythonService;
    private final MLPythonService mlPythonService;

    @PostMapping("/before")
    public ResponseEntity<?> doSomethingBefore() {
        simplePythonService.doSomethingWithPythonBefore();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/inside")
    public ResponseEntity<?> doSomethingInside() {
        simplePythonService.doSomethingWithPythonInside();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/after")
    public ResponseEntity<?> doSomethingAfter() {
        simplePythonService.doSomethingWithPythonAfter();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculator")
    public ResponseEntity<Double> calculatePrice(@RequestBody CalculatorRequest request) {
        return ResponseEntity.ok(priceCalculatorPythonService.calculatePrice(request.product(), request.customer()));
    }

    @PostMapping("/ml1")
    public ResponseEntity<MLScriptResponse> executeML1Script(@RequestBody MLScriptRequest mlScriptRequest) {
        return ResponseEntity.ok(mlPythonService.processML1Script(mlScriptRequest));
    }

    @PostMapping("/ml2")
    public ResponseEntity<Double> executeML2Script(@RequestBody String text) {
        return ResponseEntity.ok(mlPythonService.processML2Script(text));
    }
}
