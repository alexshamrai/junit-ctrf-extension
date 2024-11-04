package io.github.alexshamrai.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:ctrf.properties")
public interface CtrfConfig extends Config {

    @Key("ctrf.report.path")
    @DefaultValue("ctrf-report.json")
    String reportPath();

    @Key("ctrf.max.message.length")
    @DefaultValue("500")
    int maxMessageLength();

    @Key("junit.version")
    String junitVersion();

    @Key("ctrf.report.name")
    String reportName();

    @Key("ctrf.app.name")
    String appName();

    @Key("ctrf.app.version")
    String appVersion();

    @Key("ctrf.build.name")
    String buildName();

    @Key("ctrf.build.number")
    String buildNumber();

    @Key("ctrf.build.url")
    String buildUrl();

    @Key("ctrf.repository.name")
    String repositoryName();

    @Key("ctrf.repository.url")
    String repositoryUrl();

    @Key("ctrf.commit")
    String commit();

    @Key("ctrf.branch.name")
    String branchName();

    @Key("ctrf.os.platform")
    String osPlatform();

    @Key("ctrf.os.release")
    String osRelease();

    @Key("ctrf.os.version")
    String osVersion();

    @Key("ctrf.test.environment")
    String testEnvironment();
}