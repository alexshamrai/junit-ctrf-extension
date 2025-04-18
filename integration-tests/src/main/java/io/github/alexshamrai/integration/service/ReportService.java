package io.github.alexshamrai.integration.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for CTRF report processing.
 */
public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private final FileSteps fileSteps;

    public ReportService(FileSteps fileSteps) {
        this.fileSteps = fileSteps;
    }

    /**
     * Loads a CTRF report from a file.
     *
     * @param reportPath the path to the report file
     * @return the report as a JSONObject, or null if the report could not be loaded
     */
    public JSONObject loadReport(String reportPath) {
        if (!fileSteps.fileExists(reportPath)) {
            log.info("CTRF report not found at: {}", reportPath);
            return null;
        }

        try {
            String reportContent = fileSteps.readFileContent(reportPath);
            log.debug("CTRF report content: {}", reportContent);

            JSONObject reportJson = new JSONObject(reportContent);
            log.debug("CTRF report JSON keys: {}", reportJson.keySet());
            return reportJson;
        } catch (Exception e) {
            log.error("Error parsing CTRF report: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Creates a map of test method names to their statuses from a CTRF report.
     *
     * @param reportJson the CTRF report as a JSONObject
     * @return a map of test method names to their statuses
     */
    public Map<String, String> createTestStatusMap(JSONObject reportJson) {
        Map<String, String> testStatusMap = new HashMap<>();
        
        if (reportJson == null) {
            log.warn("Cannot create test status map from null report");
            return testStatusMap;
        }

        if (reportJson.has("results") && reportJson.getJSONObject("results").has("tests")) {
            JSONObject results = reportJson.getJSONObject("results");
            log.debug("Results JSON keys: {}", results.keySet());

            JSONArray tests = results.getJSONArray("tests");
            log.debug("Tests array length: {}", tests.length());

            for (int i = 0; i < tests.length(); i++) {
                JSONObject test = tests.getJSONObject(i);
                String name = test.optString("name", "");

                // Check for both "filepath" (old schema) and "filePath" (new schema)
                String filepath = test.optString("filepath", "");
                if (filepath.isEmpty()) {
                    filepath = test.optString("filePath", "");
                }

                String status = test.getString("status");

                log.debug("Test {}: name={}, filepath={}, status={}", i, name, filepath, status);

                // Use filepath + "." + name as the key
                if (!filepath.isEmpty() && !name.isEmpty()) {
                    // Remove the () from the method name if present
                    String methodName = name.endsWith("()") ? name.substring(0, name.length() - 2) : name;
                    String key = filepath + "." + methodName;
                    testStatusMap.put(key, status);
                    log.debug("Added test status: {} -> {}", key, status);
                }
            }
        } else {
            log.warn("CTRF report does not contain 'results.tests' array");
            if (reportJson.has("results")) {
                log.debug("Results JSON keys: {}", reportJson.getJSONObject("results").keySet());
            }
        }

        log.debug("Test status map: {}", testStatusMap);
        return testStatusMap;
    }

    /**
     * Gets the status of a specific test.
     *
     * @param testStatusMap the map of test method names to their statuses
     * @param className the class name of the test
     * @param methodName the method name of the test
     * @return the status of the test, or "unknown" if the test is not found
     */
    public String getTestStatus(Map<String, String> testStatusMap, String className, String methodName) {
        String key = className + "." + methodName;
        log.debug("Looking for test status for key: {}", key);
        log.debug("Available keys in testStatusMap: {}", testStatusMap.keySet());

        String status = testStatusMap.get(key);
        if (status == null) {
            log.warn("Test {} not found in the report", key);
            return "unknown";
        }

        log.debug("Found status for test {}: {}", key, status);
        return status;
    }

    /**
     * Verifies that a report has the expected structure.
     *
     * @param reportJson the CTRF report as a JSONObject
     * @return true if the report has the expected structure, false otherwise
     */
    public boolean verifyReportStructure(JSONObject reportJson) {
        if (reportJson == null) {
            log.warn("Cannot verify structure of null report");
            return false;
        }

        if (!reportJson.has("results")) {
            log.warn("CTRF report does not contain 'results' object");
            return false;
        }

        JSONObject results = reportJson.getJSONObject("results");
        
        if (!results.has("tool")) {
            log.warn("CTRF report does not contain 'results.tool' object");
            return false;
        }
        
        if (!results.has("summary")) {
            log.warn("CTRF report does not contain 'results.summary' object");
            return false;
        }
        
        if (!results.has("tests")) {
            log.warn("CTRF report does not contain 'results.tests' array");
            return false;
        }

        // Environment is not required in the new schema
        if (results.has("environment")) {
            log.debug("CTRF report has 'results.environment' object");
        } else {
            log.debug("CTRF report does not have 'results.environment' object. This is allowed in the new schema.");
        }

        return true;
    }

    /**
     * Verifies that the summary in a CTRF report is correct.
     *
     * @param reportJson the CTRF report as a JSONObject
     * @return true if the summary is correct, false otherwise
     */
    public boolean verifySummary(JSONObject reportJson) {
        if (reportJson == null || !reportJson.has("results") || !reportJson.getJSONObject("results").has("summary")) {
            log.warn("Cannot verify summary of invalid report");
            return false;
        }

        JSONObject summary = reportJson.getJSONObject("results").getJSONObject("summary");

        // Check required fields according to the new schema
        String[] requiredFields = {"tests", "passed", "failed", "skipped", "pending", "other", "start", "stop"};
        for (String field : requiredFields) {
            if (!summary.has(field)) {
                log.warn("Summary does not have '{}' field", field);
                return false;
            }
        }

        // Verify values
        if (summary.getInt("tests") < 0) {
            log.warn("Total tests should be non-negative, but was: {}", summary.getInt("tests"));
            return false;
        }
        
        if (summary.getInt("passed") < 0) {
            log.warn("Passed tests should be non-negative, but was: {}", summary.getInt("passed"));
            return false;
        }
        
        if (summary.getInt("failed") < 0) {
            log.warn("Failed tests should be non-negative, but was: {}", summary.getInt("failed"));
            return false;
        }
        
        if (summary.getInt("skipped") < 0) {
            log.warn("Skipped tests should be non-negative, but was: {}", summary.getInt("skipped"));
            return false;
        }
        
        if (summary.getInt("pending") < 0) {
            log.warn("Pending tests should be non-negative, but was: {}", summary.getInt("pending"));
            return false;
        }
        
        if (summary.getInt("other") < 0) {
            log.warn("Other tests should be non-negative, but was: {}", summary.getInt("other"));
            return false;
        }

        // Verify that the total equals the sum of all categories
        int total = summary.getInt("tests");
        int sum = summary.getInt("passed") + summary.getInt("failed") + summary.getInt("skipped") + 
                summary.getInt("pending") + summary.getInt("other");
        
        if (total != sum) {
            log.warn("Total ({}) should equal passed + failed + skipped + pending + other ({})", total, sum);
            return false;
        }

        // Verify that start and stop are valid timestamps
        if (summary.getLong("start") <= 0) {
            log.warn("Start timestamp should be positive, but was: {}", summary.getLong("start"));
            return false;
        }
        
        if (summary.getLong("stop") < summary.getLong("start")) {
            log.warn("Stop timestamp ({}) should be >= start timestamp ({})", 
                    summary.getLong("stop"), summary.getLong("start"));
            return false;
        }

        return true;
    }
}