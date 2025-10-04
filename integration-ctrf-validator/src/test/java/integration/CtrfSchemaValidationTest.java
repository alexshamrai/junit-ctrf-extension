package integration;

import io.github.alexshamrai.ctrf.model.Test.TestStatus;
import io.github.alexshamrai.integration.JsonSchemaValidator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CtrfSchemaValidationTest extends BaseIntegrationTest {

    private static final String SCHEMA_PATH = "src/test/resources/schema/ctrf-schema.json";

    private final JsonSchemaValidator schemaValidator;

    public CtrfSchemaValidationTest() {
        super();
        this.schemaValidator = new JsonSchemaValidator();
    }

    @Test
    void validateSchemaCompliance() throws IOException {
        var validationResult = schemaValidator.validateAgainstSchema(reportFile, SCHEMA_PATH);
        assertThat(validationResult.isSuccess())
            .as("Schema validation failed" + validationResult.getErrors().toString())
            .isTrue();

        assertThat(report.getReportFormat())
            .as("Report format should be 'CTRF'")
            .isEqualTo("CTRF");
        assertThat(report.getSpecVersion())
            .as("Spec version should match pattern x.y.z")
            .matches("^[0-9]+\\.[0-9]+\\.[0-9]+$");

        assertThat(report.getResults()).isNotNull();

        assertThat(report.getResults().getTool()).isNotNull();
        assertThat(report.getResults().getTool().getName())
            .as("Tool name should not be null or empty")
            .isNotNull()
            .isNotEmpty();

        assertThat(report.getResults().getSummary()).isNotNull();
        assertThat(report.getResults().getSummary().getTests())
            .as("Summary tests count should be a non-negative integer")
            .isGreaterThanOrEqualTo(0);
        assertThat(report.getResults().getSummary().getPassed())
            .as("Summary passed count should be a non-negative integer")
            .isGreaterThanOrEqualTo(0);
        assertThat(report.getResults().getSummary().getFailed())
            .as("Summary failed count should be a non-negative integer")
            .isGreaterThanOrEqualTo(0);
        assertThat(report.getResults().getSummary().getSkipped())
            .as("Summary skipped count should be a non-negative integer")
            .isGreaterThanOrEqualTo(0);
        assertThat(report.getResults().getSummary().getPending())
            .as("Summary pending count should be a non-negative integer")
            .isGreaterThanOrEqualTo(0);
        assertThat(report.getResults().getSummary().getOther())
            .as("Summary other count should be a non-negative integer")
            .isGreaterThanOrEqualTo(0);
        assertThat(report.getResults().getSummary().getStart())
            .as("Summary start timestamp should be a non-negative integer")
            .isGreaterThanOrEqualTo(0);
        assertThat(report.getResults().getSummary().getStop())
            .as("Summary stop timestamp should be a non-negative integer")
            .isGreaterThanOrEqualTo(0);

        validateTests(report.getResults().getTests());
    }

    private void validateTests(List<io.github.alexshamrai.ctrf.model.Test> tests) {
        assertThat(tests)
            .as("Tests list should not be null or empty")
            .isNotNull()
            .isNotEmpty();

        tests.forEach(test -> {
            assertThat(test.getName())
                .as("Test name should not be null or empty")
                .isNotNull()
                .isNotEmpty();

            assertThat(test.getStatus())
                .as("Test status should be one of the valid enum values")
                .isNotNull()
                .satisfies(status -> {
                    assertThat(status.name())
                        .isIn(TestStatus.PASSED.name(), TestStatus.FAILED.name(),
                            TestStatus.SKIPPED.name(), TestStatus.PENDING.name(),
                            TestStatus.OTHER.name());
                });
        });
    }

}
