package io.github.alexshamrai.integration.service;

import com.networknt.schema.ValidationMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Service class for test report operations.
 * Encapsulates JSONObject logic for test reports.
 */
public class TestReportSteps {
    private static final Logger log = LoggerFactory.getLogger(TestReportSteps.class);
    
    private final ReportService reportService;
    private final SchemaValidator schemaValidator;
    
    private JSONObject reportJson;
    private Map<String, String> testStatusMap;
    
    public TestReportSteps(ReportService reportService, SchemaValidator schemaValidator) {
        this.reportService = reportService;
        this.schemaValidator = schemaValidator;
    }
    
    /**
     * Loads a report from the specified path and initializes the test status map.
     *
     * @param reportPath the path to the report file
     * @return true if the report was loaded successfully, false otherwise
     */
    public boolean loadReport(String reportPath) {
        reportJson = reportService.loadReport(reportPath);
        if (reportJson != null) {
            testStatusMap = reportService.createTestStatusMap(reportJson);
            log.info("Loaded report with {} test statuses", testStatusMap.size());
            return true;
        } else {
            log.warn("Failed to load report from {}", reportPath);
            return false;
        }
    }
    
    /**
     * Validates the report against the specified schema.
     *
     * @param schemaPath the path to the schema file
     * @return a set of validation messages, empty if validation is successful
     * @throws IOException if an I/O error occurs
     */
    public Set<ValidationMessage> validateAgainstSchema(String schemaPath) throws IOException {
        if (reportJson == null) {
            throw new IllegalStateException("Report not loaded. Call loadReport() first.");
        }
        return schemaValidator.validateAgainstSchema(reportJson, schemaPath);
    }
    
    /**
     * Checks if validation errors are related to missing new required fields.
     *
     * @param validationResult the set of validation messages
     * @param fieldNames the names of fields to check
     * @return true if errors are only related to the specified fields, false otherwise
     */
    public boolean isMissingRequiredFields(Set<ValidationMessage> validationResult, String... fieldNames) {
        return schemaValidator.isMissingRequiredFields(validationResult, fieldNames);
    }
    
    /**
     * Verifies that the report has the expected structure.
     *
     * @return true if the report has the expected structure, false otherwise
     */
    public boolean verifyReportStructure() {
        if (reportJson == null) {
            throw new IllegalStateException("Report not loaded. Call loadReport() first.");
        }
        return reportService.verifyReportStructure(reportJson);
    }
    
    /**
     * Verifies that the summary in the report is correct.
     *
     * @return true if the summary is correct, false otherwise
     */
    public boolean verifySummary() {
        if (reportJson == null) {
            throw new IllegalStateException("Report not loaded. Call loadReport() first.");
        }
        return reportService.verifySummary(reportJson);
    }
    
    /**
     * Checks if the report has a specific field.
     *
     * @param fieldName the name of the field to check
     * @return true if the report has the field, false otherwise
     */
    public boolean hasField(String fieldName) {
        if (reportJson == null) {
            throw new IllegalStateException("Report not loaded. Call loadReport() first.");
        }
        return reportJson.has(fieldName);
    }
    
    /**
     * Gets a string value from the report.
     *
     * @param fieldName the name of the field to get
     * @return the string value of the field
     */
    public String getString(String fieldName) {
        if (reportJson == null) {
            throw new IllegalStateException("Report not loaded. Call loadReport() first.");
        }
        return reportJson.getString(fieldName);
    }
    
    /**
     * Gets the test status map.
     *
     * @return the test status map
     */
    public Map<String, String> getTestStatusMap() {
        if (testStatusMap == null) {
            throw new IllegalStateException("Test status map not initialized. Call loadReport() first.");
        }
        return testStatusMap;
    }
    
    /**
     * Checks if the report exists and has the expected structure.
     *
     * @return true if the report exists and has the expected structure, false otherwise
     */
    public boolean reportExists() {
        return reportJson != null && 
               reportJson.has("results") && 
               testStatusMap != null && 
               !testStatusMap.isEmpty();
    }
}