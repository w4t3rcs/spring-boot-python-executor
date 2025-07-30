package io.w4t3rcs.python.resolver;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
public interface PythonResolverHolder extends Iterable<PythonResolver> {

    @Override
    default Iterator<PythonResolver> iterator() {
        return this.getResolvers().iterator();
    }

    @Override
    default void forEach(Consumer<? super PythonResolver> action) {
        this.getResolvers().forEach(action);
    }

    @Override
    default Spliterator<PythonResolver> spliterator() {
        return this.getResolvers().spliterator();
    }

    default Stream<PythonResolver> stream() {
        return this.getResolvers().stream();
    }

    String resolveAll(String script, Map<String, Object> arguments);

    List<PythonResolver> getResolvers();
}
