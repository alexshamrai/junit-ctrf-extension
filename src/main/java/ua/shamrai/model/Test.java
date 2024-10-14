package ua.shamrai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

    private String name;
    private TestStatus status;
    private long duration;
    private Long start;
    private Long stop;
    private String suite;
    private String message;
    private String trace;
    private Integer line;
    private String ai;
    private String rawStatus;
    private List<String> tags;
    private String type;
    private String filepath;
    private Integer retries;
    private Boolean flaky;
    private String browser;
    private String device;
    private String screenshot;
    private Map<String, Object> parameters;
    private List<Step> steps;
    private Extra extra;

    public enum TestStatus {
        PASSED,
        FAILED,
        SKIPPED,
        PENDING,
        OTHER
    }
}