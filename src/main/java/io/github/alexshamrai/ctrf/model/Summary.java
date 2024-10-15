package io.github.alexshamrai.ctrf.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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