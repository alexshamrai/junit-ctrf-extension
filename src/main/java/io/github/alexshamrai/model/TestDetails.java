package io.github.alexshamrai.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class TestDetails {
    private long startTime;
    private Set<String> tags;
    private String filePath;
    private String uniqueId;
    private String displayName;
}