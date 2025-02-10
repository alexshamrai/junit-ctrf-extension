package io.github.alexshamrai.util;

import io.github.alexshamrai.BaseTest;
import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Test.TestStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SummaryUtilTest extends BaseTest {

    @org.junit.jupiter.api.Test
    void shouldCreateSummaryWithCorrectCounts() {
        List<Test> tests = List.of(
            Test.builder().status(TestStatus.PASSED).build(),
            Test.builder().status(TestStatus.FAILED).build(),
            Test.builder().status(TestStatus.PENDING).build(),
            Test.builder().status(TestStatus.SKIPPED).build(),
            Test.builder().status(TestStatus.OTHER).build()
        );

        long startTime = 1000L;
        long stopTime = 2000L;

        Summary summary = SummaryUtil.createSummary(tests, startTime, stopTime);

        assertEquals(5, summary.getTests());
        assertEquals(1, summary.getPassed());
        assertEquals(1, summary.getFailed());
        assertEquals(1, summary.getPending());
        assertEquals(1, summary.getSkipped());
        assertEquals(1, summary.getOther());
        assertEquals(startTime, summary.getStart());
        assertEquals(stopTime, summary.getStop());
    }

    @org.junit.jupiter.api.Test
    void shouldHandleEmptyTestList() {
        List<Test> tests = List.of();

        long startTime = 1500L;
        long stopTime = 2500L;

        Summary summary = SummaryUtil.createSummary(tests, startTime, stopTime);

        assertEquals(0, summary.getTests());
        assertEquals(0, summary.getPassed());
        assertEquals(0, summary.getFailed());
        assertEquals(0, summary.getPending());
        assertEquals(0, summary.getSkipped());
        assertEquals(0, summary.getOther());
        assertEquals(startTime, summary.getStart());
        assertEquals(stopTime, summary.getStop());
    }

    @org.junit.jupiter.api.Test
    void shouldCountCorrectStatusCountsWhenMultipleTestsHaveSameStatus() {
        List<Test> tests = List.of(
            Test.builder().status(TestStatus.PASSED).build(),
            Test.builder().status(TestStatus.PASSED).build(),
            Test.builder().status(TestStatus.FAILED).build(),
            Test.builder().status(TestStatus.FAILED).build(),
            Test.builder().status(TestStatus.PASSED).build()
        );

        long startTime = 1200L;
        long stopTime = 2200L;

        Summary summary = SummaryUtil.createSummary(tests, startTime, stopTime);

        assertEquals(5, summary.getTests());
        assertEquals(3, summary.getPassed());
        assertEquals(2, summary.getFailed());
        assertEquals(0, summary.getPending());
        assertEquals(0, summary.getSkipped());
        assertEquals(0, summary.getOther());
        assertEquals(startTime, summary.getStart());
        assertEquals(stopTime, summary.getStop());
    }

    @org.junit.jupiter.api.Test
    void shouldHandleNullStatusInTests() {
        List<Test> tests = List.of(
            Test.builder().status(null).build(),
            Test.builder().status(TestStatus.PASSED).build()
        );

        long startTime = 1300L;
        long stopTime = 2300L;

        Summary summary = SummaryUtil.createSummary(tests, startTime, stopTime);

        assertEquals(2, summary.getTests());
        assertEquals(1, summary.getPassed());
        assertEquals(0, summary.getFailed());
        assertEquals(0, summary.getPending());
        assertEquals(0, summary.getSkipped());
        assertEquals(0, summary.getOther());
        assertEquals(startTime, summary.getStart());
        assertEquals(stopTime, summary.getStop());
    }
}