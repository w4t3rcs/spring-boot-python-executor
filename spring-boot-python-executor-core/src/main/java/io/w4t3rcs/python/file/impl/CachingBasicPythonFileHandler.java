package io.w4t3rcs.python.file.impl;

import io.w4t3rcs.python.exception.PythonScriptPathGettingException;
import io.w4t3rcs.python.exception.PythonScriptReadingFromFileException;
import io.w4t3rcs.python.properties.PythonFileProperties;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class CachingBasicPythonFileHandler extends BasicPythonFileHandler {
    private final Map<Path, String> scriptBodyCache = new ConcurrentHashMap<>();
    private final Map<String, Path> pathCache = new ConcurrentHashMap<>();

    public CachingBasicPythonFileHandler(PythonFileProperties fileProperties) {
        super(fileProperties);
    }

    @Override
    public String readScriptBodyFromFile(Path path) {
        if (scriptBodyCache.containsKey(path)) return scriptBodyCache.get(path);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String scriptBody = bufferedReader.lines().collect(Collectors.joining("\n"));
            scriptBodyCache.put(path, scriptBody);
            return scriptBody;
        } catch (IOException e) {
            throw new PythonScriptReadingFromFileException(e);
        }
    }

    @Override
    public String readScriptBodyFromFile(Path path, UnaryOperator<String> mapper) {
        if (scriptBodyCache.containsKey(path)) return scriptBodyCache.get(path);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String scriptBody = bufferedReader.lines()
                    .map(mapper)
                    .collect(Collectors.joining("\n"));
            scriptBodyCache.put(path, scriptBody);
            return scriptBody;
        } catch (IOException e) {
            throw new PythonScriptReadingFromFileException(e);
        }
    }

    @Override
    public Path getScriptPath(String path) {
        try {
            if (pathCache.containsKey(path)) return pathCache.get(path);
            ClassPathResource classPathResource = new ClassPathResource(getFileProperties().path() + path);
            Path fullPath = classPathResource.getFile().toPath();
            pathCache.put(path, fullPath);
            return fullPath;
        } catch (IOException e) {
            throw new PythonScriptPathGettingException(e);
        }
    }
}
