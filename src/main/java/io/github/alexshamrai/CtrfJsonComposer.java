package io.github.alexshamrai;

import io.github.alexshamrai.ctrf.model.CtrfJson;
import io.github.alexshamrai.ctrf.model.Environment;
import io.github.alexshamrai.ctrf.model.Results;
import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Tool;

import java.util.List;

import static io.github.alexshamrai.config.ConfigReader.getAppName;
import static io.github.alexshamrai.config.ConfigReader.getAppVersion;
import static io.github.alexshamrai.config.ConfigReader.getBranchName;
import static io.github.alexshamrai.config.ConfigReader.getBuildName;
import static io.github.alexshamrai.config.ConfigReader.getBuildNumber;
import static io.github.alexshamrai.config.ConfigReader.getBuildUrl;
import static io.github.alexshamrai.config.ConfigReader.getCommit;
import static io.github.alexshamrai.config.ConfigReader.getJUnitVersion;
import static io.github.alexshamrai.config.ConfigReader.getOsPlatform;
import static io.github.alexshamrai.config.ConfigReader.getOsRelease;
import static io.github.alexshamrai.config.ConfigReader.getOsVersion;
import static io.github.alexshamrai.config.ConfigReader.getReportName;
import static io.github.alexshamrai.config.ConfigReader.getRepositoryName;
import static io.github.alexshamrai.config.ConfigReader.getRepositoryUrl;
import static io.github.alexshamrai.config.ConfigReader.getTestEnvironment;

public class CtrfJsonComposer {

    private static final String TOOL_NAME = "JUnit";

    public static CtrfJson generateCtrfJson(Summary summary, List<Test> tests) {
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

    private static Tool composeTool() {
        var toolBuilder = Tool.builder()
            .name(TOOL_NAME)
            .version(getJUnitVersion());

        return toolBuilder.build();
    }

    private static Environment composeEnvironment() {
        return Environment.builder()
            .reportName(getReportName())
            .appName(getAppName())
            .appVersion(getAppVersion())
            .buildName(getBuildName())
            .buildNumber(getBuildNumber())
            .buildUrl(getBuildUrl())
            .repositoryName(getRepositoryName())
            .repositoryUrl(getRepositoryUrl())
            .commit(getCommit())
            .branchName(getBranchName())
            .osPlatform(getOsPlatform())
            .osRelease(getOsRelease())
            .osVersion(getOsVersion())
            .testEnvironment(getTestEnvironment())
            .build();
    }
}