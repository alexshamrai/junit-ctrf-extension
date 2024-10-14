package ua.shamrai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Summary {
    private int tests;
    private int passed;
    private int failed;
    private int pending;
    private int skipped;
    private int other;
    private Integer suites;
    private long start;
    private long stop;
    private Extra extra;
}