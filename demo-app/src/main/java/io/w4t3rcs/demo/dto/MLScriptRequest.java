package io.w4t3rcs.demo.dto;

import java.io.Serializable;
import java.util.List;

public record MLScriptRequest(List<List<Float>> data, List<String> labels, Float testSize, Integer randomState, Integer maxDepth, Integer nEstimators) implements Serializable {
}