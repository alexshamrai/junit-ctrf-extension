package io.github.alexshamrai;

import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.ctrf.model.CtrfJson;
import io.github.alexshamrai.ctrf.model.Environment;
import io.github.alexshamrai.ctrf.model.Results;
import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Tool;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Composes the final CTRF JSON object from test results and configuration.
 * <p>
 * This class is responsible for creating the complete CTRF JSON structure according
 * to the CTRF standard, including tool information, test environment details,
 * and test results.
 */
@RequiredArgsConstructor
public class CtrfJsonComposer {

    private static final String TOOL_NAME = "JUnit";
    private final ConfigReader configReader;

    /**
     * Generates a complete CTRF JSON object containing test results and metadata.
     *
     * @param summary the test execution summary
     * @param tests the list of test results
     * @return a complete CTRF JSON object ready for serialization
     */
    public CtrfJson generateCtrfJson(Summary summary, List<Test> tests) {
        var results = Results.builder()
            .tool(composeTool())
            .summary(summary)
            .tests(tests)
            .environment(composeEnvironment())
            .build();
        return CtrfJson.builder()
            .results(results)
            .build();
    }

    /**
     * Creates the tool section of the CTRF JSON, which identifies JUnit as the test tool.
     *
     * @return a Tool object containing JUnit information
     */
    private Tool composeTool() {
        var toolBuilder = Tool.builder()
            .name(TOOL_NAME)
            .version(configReader.getJUnitVersion());

        return toolBuilder.build();
    }

    /**
     * Creates the environment section of the CTRF JSON, including details about
     * the application, build, repository, operating system, and test environment.
     *
     * @return an Environment object containing all available environment information
     */
    private Environment composeEnvironment() {
        return Environment.builder()
            .reportName(configReader.getReportName())
            .appName(configReader.getAppName())
            .appVersion(configReader.getAppVersion())
            .buildName(configReader.getBuildName())
            .buildNumber(configReader.getBuildNumber())
            .buildUrl(configReader.getBuildUrl())
            .repositoryName(configReader.getRepositoryName())
            .repositoryUrl(configReader.getRepositoryUrl())
            .commit(configReader.getCommit())
            .branchName(configReader.getBranchName())
            .osPlatform(configReader.getOsPlatform())
            .osRelease(configReader.getOsRelease())
            .osVersion(configReader.getOsVersion())
            .testEnvironment(configReader.getTestEnvironment())
            .build();
    }
}
