package io.w4t3rcs.python.resolver;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BasicPythonResolverHolder implements PythonResolverHolder {
    private final List<PythonResolver> pythonResolvers;

    @Override
    public String resolveAll(String script, Map<String, Object> arguments) {
        if (script == null || script.isEmpty()) throw new IllegalArgumentException("Script cannot be null or empty");
        String resolvedScript = script;
        for (PythonResolver resolver : this.getResolvers()) {
            resolvedScript = resolver.resolve(resolvedScript, arguments);
        }

        return resolvedScript;
    }

    @Override
    public List<PythonResolver> getResolvers() {
        return pythonResolvers;
    }
}
