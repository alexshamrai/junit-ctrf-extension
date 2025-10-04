package io.github.alexshamrai.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigReaderTest {

    private CtrfConfig mockConfig;

    @BeforeEach
    void setup() {
        var customConfig = new HashMap<String, String>();
        customConfig.put("ctrf.report.path", "ctrf.json");
        customConfig.put("ctrf.max.message.length", "987");
        customConfig.put("junit.version", "5.9.2");
        customConfig.put("ctrf.report.name", "DynamicTestReport");
        customConfig.put("ctrf.app.name", "DynamicTestApp");
        customConfig.put("ctrf.app.version", "3.0.0");
        customConfig.put("ctrf.build.name", "DynamicBuild");
        customConfig.put("ctrf.build.number", "1234");
        customConfig.put("ctrf.build.url", "http://dynamic.example.com/build");
        customConfig.put("ctrf.repository.name", "DynamicRepository");
        customConfig.put("ctrf.repository.url", "http://dynamic.example.com/repo");
        customConfig.put("ctrf.commit", "dynamic123456");
        customConfig.put("ctrf.branch.name", "dynamic-branch");
        customConfig.put("ctrf.os.platform", "DynamicOS");
        customConfig.put("ctrf.os.release", "DynamicRelease");
        customConfig.put("ctrf.os.version", "1.0.0");
        customConfig.put("ctrf.test.environment", "dynamic-environment");

        mockConfig = ConfigFactory.create(CtrfConfig.class, customConfig);
    }

    @Test
    void testDynamicConfigurationValues() {
        assertEquals("ctrf.json", mockConfig.reportPath());
        assertEquals(987, mockConfig.maxMessageLength());
        assertEquals("5.9.2", mockConfig.junitVersion());
        assertEquals("DynamicTestReport", mockConfig.reportName());
        assertEquals("DynamicTestApp", mockConfig.appName());
        assertEquals("3.0.0", mockConfig.appVersion());
        assertEquals("DynamicBuild", mockConfig.buildName());
        assertEquals("1234", mockConfig.buildNumber());
        assertEquals("http://dynamic.example.com/build", mockConfig.buildUrl());
        assertEquals("DynamicRepository", mockConfig.repositoryName());
        assertEquals("http://dynamic.example.com/repo", mockConfig.repositoryUrl());
        assertEquals("dynamic123456", mockConfig.commit());
        assertEquals("dynamic-branch", mockConfig.branchName());
        assertEquals("DynamicOS", mockConfig.osPlatform());
        assertEquals("DynamicRelease", mockConfig.osRelease());
        assertEquals("1.0.0", mockConfig.osVersion());
        assertEquals("dynamic-environment", mockConfig.testEnvironment());
    }

    @Test
    void testUnsetConfigurationValues() {
        var partialConfig = new HashMap<String, String>();
        partialConfig.put("ctrf.report.path", "partial-dynamic-report.json");
        mockConfig = ConfigFactory.create(CtrfConfig.class, partialConfig);

        assertEquals("partial-dynamic-report.json", mockConfig.reportPath());

        assertEquals(500, mockConfig.maxMessageLength());
        assertNull(mockConfig.junitVersion());
        assertNull(mockConfig.reportName());
        assertNull(mockConfig.appName());
    }

    @Test
    void testCompletelyDefaultValues() {
        mockConfig = ConfigFactory.create(CtrfConfig.class);

        assertEquals("ctrf-report.json", mockConfig.reportPath());
        assertEquals(500, mockConfig.maxMessageLength());

        assertNull(mockConfig.junitVersion());
        assertNull(mockConfig.reportName());
        assertNull(mockConfig.appName());
        assertNull(mockConfig.appVersion());
        assertNull(mockConfig.buildName());
        assertNull(mockConfig.buildNumber());
        assertNull(mockConfig.buildUrl());
        assertNull(mockConfig.repositoryName());
        assertNull(mockConfig.repositoryUrl());
        assertNull(mockConfig.commit());
        assertNull(mockConfig.branchName());
        assertNull(mockConfig.osPlatform());
        assertNull(mockConfig.osRelease());
        assertNull(mockConfig.osVersion());
        assertNull(mockConfig.testEnvironment());
    }
}