# Integration Tests for JUnit CTRF Extension

This document provides a comprehensive explanation of the integration tests for the JUnit CTRF Extension project. These tests validate that the extension correctly generates CTRF-compliant reports and handles various test scenarios properly.

## Overview

The integration tests serve several key purposes:

1. **Validate CTRF Report Generation**: Ensure the extension generates valid CTRF-compliant JSON reports
2. **Verify Report Schema Compliance**: Check that the generated reports adhere to the CTRF schema specification
3. **Test Logical Correctness**: Confirm that test statuses, durations, and other metadata are correctly captured
4. **Simulate Real-World Scenarios**: Test various test outcomes (success, failure, skipped) and configurations(parallel execution, dynamic parameters, etc.)
5. **Enable reporter both Listener and as Extension**: Verify that the extension can be used both as a Listener and as an Extension


## Integration Test Structure

The integration tests are organized in separate modules (`integration-ctrf-validator`, `integration-tests-extension`, `integration-tests-listener` ).  The `integration-tests-extension` and `integration-tests-listener` modules contain fakes tests to generate CTRF report with required structure and test outcomes. Their structure is identical, the only difference is the way that CTRF reporter is enabled.
```
integration-tests-listener/
├── build.gradle                 
├── src/
    ├── test/
        ├── java/
        │   └── io/github/alexshamrai/integration/fake
        │       ├── BaseFakeTest.java          # Base class for all fake tests
        │       ├── DummyDisabledTest.java     # Tests with @Disabled annotation
        │       ├── DummyFailedTest.java       # Tests designed to fail
        │       ├── DummySuccessTest.java      # Tests designed to succeed
        │       ├── FirstLongTest.java         # Tests with specific durations
        │       └── SecondLongTest.java        # More tests with specific durations
```

### Fake Test Classes

The fake test classes are designed to simulate various test scenarios:

#### BaseFakeTest

```java
@ExtendWith(CtrfExtension.class) //only for extension
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


The `integration-ctrf-validator` module contains validation logic for json file that`integration-tests-extension` and `integration-tests-listener` modules create after test execution.
```
integration-tests/
├── build.gradle                 # Integration tests build configuration
├── src/
    ├── test/
        ├── java/
        │   └── io/github/alexshamrai/integration/
        │       ├── CtrfSchemaValidationTest.java  # Validates report schema compliance
        │       ├── CtrfLogicTest.java             # Verifies logical correctness of the report
        └── resources/
            └── schema/
                └── ctrf-schema.json               # CTRF schema definition
```

## Test Execution Flow

The integration tests follow a two-phase execution process:

1. **Report Generation Phase**: 
   - Executes the fake test classes to generate a CTRF report
   - Uses the `integration-tests-listener:test` or `integration-tests-extension:test` Gradle task
   - Ignores test failures (since some tests are designed to fail)

2. **Validation Phase**:
   - Validates the generated report against the CTRF schema
   - Verifies the logical correctness of the report content
   - Uses the `integration-ctrf-validator:test` Gradle task
   - Runs `CtrfSchemaValidationTest` and `CtrfLogicTest`

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

To run the integration tests you should consequently execute tests from 2 modules
```bash
# Generate the CTRF report only
./gradlew clean :integration-tests-listener:test 

# Validate the generated report only
./gradlew :integration-ctrf-validator:test
````

### Parallel Test Execution

The integration tests support parallel execution through the `threads` system property:
```bash
./gradlew :integration-tests-listener:test -Dthreads=4
```

This configures JUnit to run tests concurrently, which helps verify that the CTRF extension correctly handles parallel test execution.

## Run tests exactly as in CI

To run the integration tests exactly as in CI, in order to verify all the parameters processing locally (dynamic parameters, like build.number and build.url are hardcoded here):
```bash
./gradlew clean :integration-tests-listener:test -Dthreads=2 -Dctrf.build.name=system-build -Dctrf.build.number=777 -Dctrf.build.url=https://github.com/alexshamrai/junit-ctrf-extension/actions/runs/12345678
```

```bash
./gradlew :integration-ctrf-validator:test -Dctrf.report.path=../integration-tests-listener/build/test-results/ctrf-report.json
```
If you want to run integration tests locally, run them exactly as in CI for the module `integration-tests-listener` or `integration-tests-extension`

## Conclusion

The integration tests provide comprehensive validation of the JUnit CTRF Extension's functionality. They ensure that the extension generates valid CTRF reports that accurately reflect test execution results across various scenarios, including different test statuses, durations, and parallel execution.

