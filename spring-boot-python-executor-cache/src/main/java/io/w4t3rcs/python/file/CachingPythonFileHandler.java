package io.w4t3rcs.python.file;

import io.w4t3rcs.python.exception.PythonCacheException;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.nio.file.Path;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class CachingPythonFileHandler implements PythonFileHandler {
    private final PythonFileHandler pythonFileHandler;
    private final Cache scriptBodyCache;
    private final Cache pathCache;

    public CachingPythonFileHandler(PythonCacheProperties cacheProperties, PythonFileHandler pythonFileHandler, CacheManager cacheManager) {
        this.pythonFileHandler = pythonFileHandler;
        var fileProperties = cacheProperties.file();
        this.pathCache = cacheManager.getCache(fileProperties.pathsName());
        this.scriptBodyCache = cacheManager.getCache(fileProperties.bodiesName());
    }

    @Override
    public boolean isPythonFile(String path) {
        return pythonFileHandler.isPythonFile(path);
    }

    @Override
    public void writeScriptBodyToFile(String path, String script) {
        this.writeScriptBodyToFile(this.getScriptPath(path), script);
    }

    @Override
    public void writeScriptBodyToFile(Path path, String script) {
        try {
            scriptBodyCache.evictIfPresent(path);
            pythonFileHandler.writeScriptBodyToFile(path, script);
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }

    @Override
    public String readScriptBodyFromFile(String path) {
        return this.readScriptBodyFromFile(this.getScriptPath(path));
    }

    @Override
    public String readScriptBodyFromFile(Path path) {
        try {
            String cachedScriptBody = scriptBodyCache.get(path, String.class);
            if (cachedScriptBody != null) return cachedScriptBody;
            String scriptBody = pythonFileHandler.readScriptBodyFromFile(path);
            scriptBodyCache.put(path, scriptBody);
            return scriptBody;
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }

    @Override
    public String readScriptBodyFromFile(String path, UnaryOperator<String> mapper) {
        return this.readScriptBodyFromFile(this.getScriptPath(path), mapper);
    }

    @Override
    public String readScriptBodyFromFile(Path path, UnaryOperator<String> mapper) {
        try {
            String cachedScriptBody = scriptBodyCache.get(path, String.class);
            if (cachedScriptBody != null) return cachedScriptBody.lines()
                    .map(mapper)
                    .collect(Collectors.joining("\n"));
            String scriptBody = pythonFileHandler.readScriptBodyFromFile(path);
            scriptBodyCache.put(path, scriptBody);
            return scriptBody.lines()
                    .map(mapper)
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }

    @Override
    public Path getScriptPath(String path) {
        try {
            Path cachedPath = pathCache.get(path, Path.class);
            if (cachedPath != null) return cachedPath;
            Path fullPath = pythonFileHandler.getScriptPath(path);
            pathCache.put(path, fullPath);
            return fullPath;
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }
}