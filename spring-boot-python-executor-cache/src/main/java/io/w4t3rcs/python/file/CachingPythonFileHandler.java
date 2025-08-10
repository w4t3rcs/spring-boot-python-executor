package io.w4t3rcs.python.file;

import io.w4t3rcs.python.exception.PythonCacheException;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.nio.file.Path;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * {@link PythonFileHandler} implementation that adds caching capabilities
 * for script file paths and script bodies.
 * <p>
 * This handler delegates all operations to a wrapped {@link PythonFileHandler} instance,
 * caching file paths and script contents to improve performance for repeated access.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * PythonFileHandler baseHandler = ...;
 * CacheManager cacheManager = ...;
 * PythonCacheProperties cacheProperties = ...;
 * PythonFileHandler cachingHandler = new CachingPythonFileHandler(cacheProperties, baseHandler, cacheManager);
 *
 * Path scriptPath = cachingHandler.getScriptPath("script.py");
 * String scriptBody = cachingHandler.readScriptBodyFromFile(scriptPath);
 * }</pre>
 *
 * @see PythonFileHandler
 * @see PythonCacheProperties.NameProperties
 * @author w4t3rcs
 * @since 1.0.0
 */
public class CachingPythonFileHandler implements PythonFileHandler {
    private final PythonFileHandler pythonFileHandler;
    private final Cache scriptBodyCache;
    private final Cache pathCache;

    /**
     * Constructs a new {@code CachingPythonFileHandler}.
     *
     * @param cacheProperties non-null cache properties providing cache names
     * @param pythonFileHandler non-null delegate {@link PythonFileHandler} instance
     * @param cacheManager non-null {@link CacheManager} to obtain cache instances
     */
    public CachingPythonFileHandler(PythonCacheProperties cacheProperties, PythonFileHandler pythonFileHandler, CacheManager cacheManager) {
        this.pythonFileHandler = pythonFileHandler;
        var nameProperties = cacheProperties.name();
        this.pathCache = cacheManager.getCache(nameProperties.filePaths());
        this.scriptBodyCache = cacheManager.getCache(nameProperties.fileBodies());
    }

    /**
     * Checks whether the given path corresponds to a Python file.
     * Delegates to the underlying {@link PythonFileHandler}.
     *
     * @param path non-null string path to check
     * @return true if the path is a Python file, false otherwise
     */
    @Override
    public boolean isPythonFile(String path) {
        return pythonFileHandler.isPythonFile(path);
    }

    /**
     * Writes the given script body to the file at the specified path.
     * <p>
     * The cache for the script body at the resolved path is evicted before writing.
     * </p>
     *
     * @param path non-null string path to the script file
     * @param script non-null script body to write
     * @throws PythonCacheException if any underlying error occurs during caching or writing
     */
    @Override
    public void writeScriptBodyToFile(String path, String script) {
        this.writeScriptBodyToFile(this.getScriptPath(path), script);
    }

    /**
     * Writes the given script body to the file at the specified path.
     * <p>
     * The cache for the script body at the given {@link Path} is evicted before writing.
     * </p>
     *
     * @param path non-null {@link Path} to the script file
     * @param script non-null script body to write
     * @throws PythonCacheException if any underlying error occurs during caching or writing
     */
    @Override
    public void writeScriptBodyToFile(Path path, String script) {
        try {
            scriptBodyCache.evictIfPresent(path);
            pythonFileHandler.writeScriptBodyToFile(path, script);
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }

    /**
     * Reads the script body from the file at the specified path.
     * <p>
     * Results are cached by resolved {@link Path}. If a cached value is present, it is returned.
     * Otherwise, the script is read from the underlying handler and cached.
     * </p>
     *
     * @param path non-null string path to the script file
     * @return non-null script body
     * @throws PythonCacheException if any underlying error occurs during caching or reading
     */
    @Override
    public String readScriptBodyFromFile(String path) {
        return this.readScriptBodyFromFile(this.getScriptPath(path));
    }

    /**
     * Reads the script body from the file at the specified path.
     * <p>
     * Results are cached by {@link Path}. If a cached value is present, it is returned.
     * Otherwise, the script is read from the underlying handler and cached.
     * </p>
     *
     * @param path non-null {@link Path} to the script file
     * @return non-null script body
     * @throws PythonCacheException if any underlying error occurs during caching or reading
     */
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

    /**
     * Reads the script body from the file at the specified path,
     * applying the given mapping function to each line of the script body.
     * <p>
     * Cached content is mapped on retrieval. If not cached, the content is read, cached,
     * then mapped before returning.
     * </p>
     *
     * @param path non-null string path to the script file
     * @param mapper non-null {@link UnaryOperator} function to map each line of the script
     * @return non-null mapped script body
     * @throws PythonCacheException if any underlying error occurs during caching or reading
     */
    @Override
    public String readScriptBodyFromFile(String path, UnaryOperator<String> mapper) {
        return this.readScriptBodyFromFile(this.getScriptPath(path), mapper);
    }

    /**
     * Reads the script body from the file at the specified path,
     * applying the given mapping function to each line of the script body.
     * <p>
     * Cached content is mapped on retrieval. If not cached, the content is read, cached,
     * then mapped before returning.
     * </p>
     *
     * @param path non-null {@link Path} to the script file
     * @param mapper non-null {@link UnaryOperator} function to map each line of the script
     * @return non-null mapped script body
     * @throws PythonCacheException if any underlying error occurs during caching or reading
     */
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

    /**
     * Resolves the full {@link Path} to the script file given a string path.
     * <p>
     * The resolved path is cached. If present in cache, the cached path is returned.
     * Otherwise, the path is resolved by the underlying handler and cached before returning.
     * </p>
     *
     * @param path non-null string path to resolve
     * @return non-null resolved {@link Path}
     * @throws PythonCacheException if any underlying error occurs during caching or resolution
     */
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