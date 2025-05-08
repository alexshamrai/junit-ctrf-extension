# CTRF Timeline Visualization Gradle Task

A Gradle task for visualizing test execution timelines from CTRF (Common Test Report Format) JSON reports and
integrating them into the Gradle test report.

## Overview

This Gradle task generates an interactive HTML visualization of test execution timelines from a CTRF JSON report and
injects it into the standard Gradle test report. The visualization shows when each test started and ended, along with
test names, timestamps, and UTC time.

## Usage

The task is automatically executed after the test task completes. No manual intervention is required.

### Configuration

The task is configured in `gradle/timeline-visualization.gradle` and is applied to the project in `build.gradle`:

```gradle
apply from: 'gradle/timeline-visualization.gradle'
test.finalizedBy(addTimelineVisualization)
```

## Implementation Details

The task:

1. Reads the CTRF JSON report from `build/test-results/test/ctrf-report.json`
2. Parses the JSON to extract test data and summary information
3. Generates HTML for the timeline visualization
4. Injects the HTML into the Gradle test report at `build/reports/tests/test/index.html`

## Error Handling

The task is designed to be fault-tolerant and will not fail the build if it encounters errors. If any exceptions occur
during the task execution (e.g., missing files, JSON parsing errors, etc.), the task will:

1. Log a warning message with the error details
2. Print the stack trace for debugging purposes
3. Allow the build to continue without failing

This ensures that issues with the timeline visualization generation don't affect the overall build result.
