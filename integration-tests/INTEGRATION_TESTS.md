# Integration Tests for JUnit CTRF Extension

This document provides a comprehensive explanation of the integration tests for the JUnit CTRF Extension project. These tests validate that the extension correctly generates CTRF-compliant reports and handles various test scenarios properly.

## Overview

The integration tests serve several key purposes:

1. **Validate CTRF Report Generation**: Ensure the extension generates valid CTRF-compliant JSON reports
2. **Verify Report Schema Compliance**: Check that the generated reports adhere to the CTRF schema specification
3. **Test Logical Correctness**: Confirm that test statuses, durations, and other metadata are correctly captured
4. **Simulate Real-World Scenarios**: Test various test outcomes (success, failure, skipped) and configurations

## Integration Test Structure

The integration tests are organized in a separate Gradle module (`integration-tests`) with the following structure:

```
integration-tests/
├── build.gradle                 # Integration tests build configuration
├── src/
    ├── test/
        ├── java/
        │   └── io/github/alexshamrai/integration/
        │       ├── CtrfSchemaValidationTest.java  # Validates report schema compliance
        │       ├── CtrfLogicTest.java             # Verifies logical correctness of the report
        │       └── fake/                          # Fake tests to generate the CTRF report
        │           ├── BaseFakeTest.java          # Base class for all fake tests
        │           ├── DummyDisabledTest.java     # Tests with @Disabled annotation
        │           ├── DummyFailedTest.java       # Tests designed to fail
        │           ├── DummySuccessTest.java      # Tests designed to succeed
        │           ├── FirstLongTest.java         # Tests with specific durations
        │           └── SecondLongTest.java        # More tests with specific durations
        └── resources/
            └── schema/
                └── ctrf-schema.json               # CTRF schema definition
```

## Test Execution Flow

The integration tests follow a two-phase execution process:

1. **Report Generation Phase**: 
   - Executes the fake test classes to generate a CTRF report
   - Uses the `generateCtrfReport` Gradle task
   - Includes only tests tagged with "fake"
   - Ignores test failures (since some tests are designed to fail)

2. **Validation Phase**:
   - Validates the generated report against the CTRF schema
   - Verifies the logical correctness of the report content
   - Uses the `validateCtrfReport` Gradle task
   - Runs `CtrfSchemaValidationTest` and `CtrfLogicTest`

## Key Components

### Fake Test Classes

The fake test classes are designed to simulate various test scenarios:

#### BaseFakeTest

```java
@ExtendWith(CtrfExtension.class)
@Tag("fake")
public abstract class BaseFakeTest {
    // Common lifecycle methods
}
```

- Base class for all fake tests
- Applies the CTRF extension to all subclasses
- Tagged with "fake" for selective execution
- Provides common lifecycle methods

#### Test Status Verification

- **DummySuccessTest**: Contains tests that pass successfully
- **DummyFailedTest**: Contains tests that intentionally fail
- **DummyDisabledTest**: Contains tests marked with `@Disabled`

#### Test Duration Verification

- **FirstLongTest** and **SecondLongTest**: Contain tests with specific sleep durations (0.5s, 1s, 2s)
- Used to verify that test durations are correctly captured in the report

### Validation Tests

#### CtrfSchemaValidationTest

This test validates that the generated report complies with the CTRF schema:

- Verifies the report against the JSON schema definition
- Checks that required fields are present and have correct formats
- Validates that the report format is "CTRF" and the spec version follows the pattern x.y.z
- Ensures all test entries have valid names and statuses

#### CtrfLogicTest

This test verifies the logical correctness of the report content:

- Checks that test statuses are correctly reported (passed, failed, skipped)
- Verifies that summary statistics match the actual test results
- Validates that test durations are accurately captured, especially for long-running tests

## Running Integration Tests

To run the integration tests:

```bash
./gradlew :integration-tests:validateCtrfReport
```

This command will:
1. Generate the CTRF report by running the fake tests
2. Validate the generated report against the schema and verify its logical correctness

You can also run the individual tasks:

```bash
# Generate the CTRF report only
./gradlew :integration-tests:generateCtrfReport

# Validate the generated report only
./gradlew :integration-tests:validateCtrfReport -x generateCtrfReport
```

## Parallel Test Execution

The integration tests support parallel execution through the `threads` system property:

```bash
./gradlew :integration-tests:validateCtrfReport -Dthreads=4
```

This configures JUnit to run tests concurrently, which helps verify that the CTRF extension correctly handles parallel test execution.

## Run tests exactly as in CI

To run the integration tests exactly as in CI, in order to verify all the parameters processing locally(dynamic parameters, like build.number and build.url are hardcoded here):
```bash
./gradlew clean :integration-tests:validateCtrfReport -Dthreads=2 -Dctrf.build.name=system-build -Dctrf.build.number=777 -Dctrf.build.url=https://github.com/alexshamrai/junit-ctrf-extension/actions/runs/12345678
```

## Adding New Integration Tests

To add new test scenarios:

1. Create a new test class that extends `BaseFakeTest`
2. Add the `@Tag("fake")` annotation if it should be included in report generation
3. Implement test methods that cover the desired scenarios
4. Update `CtrfLogicTest` if necessary to verify the new scenarios

## Conclusion

The integration tests provide comprehensive validation of the JUnit CTRF Extension's functionality. They ensure that the extension generates valid CTRF reports that accurately reflect test execution results across various scenarios, including different test statuses, durations, and parallel execution.