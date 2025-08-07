package io.w4t3rcs.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record MLScriptResponse(Float accuracy,
                               @JsonProperty("macro_avg") MacroAvg macroAvg,
                               @JsonProperty("model_info") ModelInfo modelInfo) implements Serializable {
    public record MacroAvg(Float precision,
                           Float recall,
                           @JsonProperty("f1-score") Float f1Score,
                           Float support) implements Serializable {
    }

    public record ModelInfo(String type,
                            @JsonProperty("max_depth") Integer maxDepth,
                            @JsonProperty("n_estimators") Integer nEstimators) implements Serializable {
    }
}
