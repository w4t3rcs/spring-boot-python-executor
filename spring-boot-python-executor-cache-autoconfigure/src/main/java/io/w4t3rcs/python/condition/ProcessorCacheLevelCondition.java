package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.properties.PythonCacheProperties;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class ProcessorCacheLevelCondition extends AbstractCacheLevelCondition {
    private final PythonCacheProperties.PythonCacheLevel cacheLevel = PythonCacheProperties.PythonCacheLevel.PROCESSOR;
}
