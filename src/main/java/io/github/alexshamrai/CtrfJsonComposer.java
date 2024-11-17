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

@RequiredArgsConstructor
public class CtrfJsonComposer {

    private static final String TOOL_NAME = "JUnit";
    private final ConfigReader configReader;

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

    private Tool composeTool() {
        var toolBuilder = Tool.builder()
            .name(TOOL_NAME)
            .version(configReader.getJUnitVersion());

        return toolBuilder.build();
    }

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