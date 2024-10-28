package io.github.alexshamrai.util;

import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;

import java.util.List;

public class SummaryCreator {

    public Summary createSummary(List<Test> tests, long startTime, long stopTime) {
        return Summary.builder()
            .tests(tests.size())
            .passed((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.PASSED).count())
            .failed((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.FAILED).count())
            .pending((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.PENDING).count())
            .skipped((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.SKIPPED).count())
            .other((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.OTHER).count())
            .start(startTime)
            .stop(stopTime)
            .build();
    }
}
