package io.github.alexshamrai;

import io.github.alexshamrai.ctrf.model.Extra;
import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * Processor responsible for calculating and setting startup duration in test results.
 * <p>
 * This class calculates the startup duration as the difference between the summary start time
 * and the first test start time, then adds it to the summary's extra data.
 */
@RequiredArgsConstructor
public class StartupDurationProcessor {

    /**
     * Processes and adds test suite startup duration to the summary if applicable.
     * <p>
     * Calculates startup duration as the difference between the earliest test start time and
     * summary start time, then adds it to the summary's extra data.
     *
     * @param summary the test execution summary
     * @param tests   the list of test results
     */
    public void processStartupDuration(Summary summary, List<Test> tests) {
        if (summary == null || tests.isEmpty()) {
            return;
        }

        var firstTestStart = findStartTimeOfTheFirstTest(tests);
        if (firstTestStart != null && summary.getStart() > 0) {
            long startupDuration = firstTestStart - summary.getStart();
            var extra = getExtraObject(summary);
            extra.setStartupDuration(startupDuration);
        }
    }

    private Extra getExtraObject(Summary summary) {
        Extra extra = summary.getExtra();
        if (extra == null) {
            extra = new Extra();
            summary.setExtra(extra);
        }
        return extra;
    }

    private Long findStartTimeOfTheFirstTest(List<Test> tests) {
        return tests.stream()
            .map(Test::getStart)
            .filter(Objects::nonNull)
            .min(Long::compareTo)
            .orElse(null);
    }
}