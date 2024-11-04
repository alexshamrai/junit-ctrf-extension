package io.github.alexshamrai.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigReader {

    private static final CtrfConfig config = ConfigFactory.create(CtrfConfig.class);

    public static String getReportPath() {
        return config.reportPath();
    }

    public static int getMaxMessageLength() {
        return config.maxMessageLength();
    }

    public static String getJUnitVersion() {
        return config.junitVersion();
    }

    public static String getReportName() {
        return config.reportName();
    }

    public static String getAppName() {
        return config.appName();
    }

    public static String getAppVersion() {
        return config.appVersion();
    }

    public static String getBuildName() {
        return config.buildName();
    }

    public static String getBuildNumber() {
        return config.buildNumber();
    }

    public static String getBuildUrl() {
        return config.buildUrl();
    }

    public static String getRepositoryName() {
        return config.repositoryName();
    }

    public static String getRepositoryUrl() {
        return config.repositoryUrl();
    }

    public static String getCommit() {
        return config.commit();
    }

    public static String getBranchName() {
        return config.branchName();
    }

    public static String getOsPlatform() {
        return config.osPlatform();
    }

    public static String getOsRelease() {
        return config.osRelease();
    }

    public static String getOsVersion() {
        return config.osVersion();
    }

    public static String getTestEnvironment() {
        return config.testEnvironment();
    }
}