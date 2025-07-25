package io.github.w4t3rcs.example.service;

import io.w4t3rcs.python.metadata.PythonAfter;
import io.w4t3rcs.python.metadata.PythonBefore;
import io.w4t3rcs.python.metadata.PythonParam;
import io.w4t3rcs.python.processor.PythonProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamplePythonService {
    private final PythonProcessor pythonProcessor;

    @PythonBefore("print(spel{#name} + ' ' + spel{#sur})")
    public void executeBefore(String name, @PythonParam("sur") String surname) {
        System.out.println("Hello from Java after Python: " + name + " : " + surname);
    }

    @PythonBefore("example.py")
    public void executeFileBefore(String name) {
        System.out.println("Hello from Java after Python file: " + name);
    }

    @PythonAfter("print(spel{#result} + ' ' + spel{#name} + ' ' + spel{#sur})")
    public String executeAfter(String name, @PythonParam("sur") String surname) {
        System.out.println("Hello from Java before Python: " + name + " : " + surname);
        return "Python app is greeting you:";
    }

    public String executeInside(String name, String surname) {
        String script = """
                test_var = spel{#name} + spel{#surname}
                o4java{test_var}
                """;
        Map<String, Object> arguments = Map.of("name", name, "surname", surname);
        return pythonProcessor.process(script, String.class, arguments);
    }
}
