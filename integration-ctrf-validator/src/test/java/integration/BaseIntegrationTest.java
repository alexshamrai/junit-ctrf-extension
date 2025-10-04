package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alexshamrai.ctrf.model.CtrfJson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

    private static final String DEFAULT_REPORT_PATH =
        "For Local Run can be used:" + "../integration-tests-extension/build/test-results/ctrf-report.json";

    protected final ObjectMapper objectMapper;
    protected CtrfJson report;
    protected File reportFile;

    protected BaseIntegrationTest() {
        this.objectMapper = new ObjectMapper();
    }

    @BeforeAll
    void setup() throws IOException {
        String reportPath = resolveReportPath();
        reportFile = new File(reportPath);

        if (!reportFile.exists()) {
            throw new IOException("Report file not found at: " + reportFile.getAbsolutePath());
        }

        report = objectMapper.readValue(reportFile, CtrfJson.class);
    }

    private static String resolveReportPath() {
        String configuredPath = System.getProperty("ctrf.report.path");
        if (configuredPath == null) {
            return DEFAULT_REPORT_PATH;
        }

        return configuredPath;
    }
}
