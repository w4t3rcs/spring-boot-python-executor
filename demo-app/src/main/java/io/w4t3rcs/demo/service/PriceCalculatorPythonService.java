package io.w4t3rcs.demo.service;

import io.w4t3rcs.demo.dto.CustomerDto;
import io.w4t3rcs.demo.dto.ProductDto;
import io.w4t3rcs.python.dto.PythonExecutionResponse;
import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PriceCalculatorPythonService {
    private static final String CALCULATOR_SCRIPT = "price_calculator.py";
    private final PythonProcessor pythonProcessor;

    public double calculatePrice(ProductDto product, CustomerDto customer) {
        int randomMultiplier = new Random().nextInt(10);
        product.setBasePrice(product.getBasePrice() * randomMultiplier);
        Map<String, Object> arguments = Map.of(
                "product", product,
                "customer", customer
        );

        PythonExecutionResponse<Double> response = pythonProcessor.process(CALCULATOR_SCRIPT, Double.class, arguments);
        return response.body();
    }
}
