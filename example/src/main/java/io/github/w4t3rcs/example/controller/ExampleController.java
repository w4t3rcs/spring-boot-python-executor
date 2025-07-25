package io.github.w4t3rcs.example.controller;

import io.github.w4t3rcs.example.service.ExamplePythonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleController {
    private final ExamplePythonService examplePythonService;

    @GetMapping(path = "/before", params = {"name", "surname"})
    public void helloBefore(@RequestParam String name, @RequestParam String surname) {
        examplePythonService.executeBefore(name, surname);
    }

    @GetMapping(path = "/before/file", params = {"name"})
    public void helloBefore(@RequestParam String name) {
        examplePythonService.executeFileBefore(name);
    }

    @GetMapping(path = "/after", params = {"name", "surname"})
    public String helloAfter(@RequestParam String name, @RequestParam String surname) {
        return examplePythonService.executeAfter(name, surname);
    }

    @GetMapping(path = "/inside", params = {"name", "surname"})
    public String helloInside(@RequestParam String name, @RequestParam String surname) {
        return examplePythonService.executeInside(name, surname);
    }
}
