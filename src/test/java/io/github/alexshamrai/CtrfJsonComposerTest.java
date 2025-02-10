package io.github.alexshamrai;

import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.ctrf.model.CtrfJson;
import io.github.alexshamrai.ctrf.model.Environment;
import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Tool;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CtrfJsonComposerTest extends BaseTest {

    @org.junit.jupiter.api.Test
    void testGenerateCtrfJsonWithValidInput() {
        Summary summary = Summary.builder().build();
        List<Test> tests = List.of(
            Test.builder().build(),
            Test.builder().build()
        );

        try (MockedStatic<ConfigReader> configMock = Mockito.mockStatic(ConfigReader.class)) {
            mockStaticConfig(configMock);

            CtrfJson result = CtrfJsonComposer.generateCtrfJson(summary, tests);

            assertNotNull(result);
            assertNotNull(result.getResults());
            assertEquals(tests, result.getResults().getTests());
            assertEquals(summary, result.getResults().getSummary());

            Tool tool = result.getResults().getTool();
            assertNotNull(tool);
            assertEquals("JUnit", tool.getName());
            assertEquals("mockedJUnitVersion", tool.getVersion());

            Environment environment = result.getResults().getEnvironment();
            assertNotNull(environment);
            assertEquals("mockedReportName", environment.getReportName());
            assertEquals("mockedAppName", environment.getAppName());
            assertEquals("mockedAppVersion", environment.getAppVersion());
            assertEquals("mockedBuildName", environment.getBuildName());
            assertEquals("mockedBuildNumber", environment.getBuildNumber());
            assertEquals("mockedBuildUrl", environment.getBuildUrl());
            assertEquals("mockedRepositoryName", environment.getRepositoryName());
            assertEquals("mockedRepositoryUrl", environment.getRepositoryUrl());
            assertEquals("mockedCommit", environment.getCommit());
            assertEquals("mockedBranchName", environment.getBranchName());
            assertEquals("mockedOsPlatform", environment.getOsPlatform());
            assertEquals("mockedOsRelease", environment.getOsRelease());
            assertEquals("mockedOsVersion", environment.getOsVersion());
            assertEquals("mockedTestEnvironment", environment.getTestEnvironment());
        }
    }

    @org.junit.jupiter.api.Test
    void testGenerateCtrfJsonWithEmptyTestList() {
        Summary summary = Summary.builder().build();
        List<Test> tests = List.of();

        try (MockedStatic<ConfigReader> configMock = Mockito.mockStatic(ConfigReader.class)) {
            mockStaticConfig(configMock);

            CtrfJson result = CtrfJsonComposer.generateCtrfJson(summary, tests);

            assertNotNull(result);
            assertNotNull(result.getResults());
            assertEquals(tests, result.getResults().getTests());
            assertEquals(summary, result.getResults().getSummary());
        }
    }

    @org.junit.jupiter.api.Test
    void testGenerateCtrfJsonWithNullSummary() {
        Summary summary = null;
        List<Test> tests = List.of(
            Test.builder().build()
        );

        try (MockedStatic<ConfigReader> configMock = Mockito.mockStatic(ConfigReader.class)) {
            mockStaticConfig(configMock);

            CtrfJson result = CtrfJsonComposer.generateCtrfJson(summary, tests);

            assertNotNull(result);
            assertNotNull(result.getResults());
            assertEquals(tests, result.getResults().getTests());
            assertEquals(summary, result.getResults().getSummary());
        }
    }

    private void mockStaticConfig(MockedStatic<ConfigReader> configMock) {
        configMock.when(ConfigReader::getJUnitVersion).thenReturn("mockedJUnitVersion");
        configMock.when(ConfigReader::getReportName).thenReturn("mockedReportName");
        configMock.when(ConfigReader::getAppName).thenReturn("mockedAppName");
        configMock.when(ConfigReader::getAppVersion).thenReturn("mockedAppVersion");
        configMock.when(ConfigReader::getBuildName).thenReturn("mockedBuildName");
        configMock.when(ConfigReader::getBuildNumber).thenReturn("mockedBuildNumber");
        configMock.when(ConfigReader::getBuildUrl).thenReturn("mockedBuildUrl");
        configMock.when(ConfigReader::getRepositoryName).thenReturn("mockedRepositoryName");
        configMock.when(ConfigReader::getRepositoryUrl).thenReturn("mockedRepositoryUrl");
        configMock.when(ConfigReader::getCommit).thenReturn("mockedCommit");
        configMock.when(ConfigReader::getBranchName).thenReturn("mockedBranchName");
        configMock.when(ConfigReader::getOsPlatform).thenReturn("mockedOsPlatform");
        configMock.when(ConfigReader::getOsRelease).thenReturn("mockedOsRelease");
        configMock.when(ConfigReader::getOsVersion).thenReturn("mockedOsVersion");
        configMock.when(ConfigReader::getTestEnvironment).thenReturn("mockedTestEnvironment");
    }
}