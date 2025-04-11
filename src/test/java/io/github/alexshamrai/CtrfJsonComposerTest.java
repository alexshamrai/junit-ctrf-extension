package io.github.alexshamrai;

import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.ctrf.model.Environment;
import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CtrfJsonComposerTest extends BaseTest {

    private ConfigReader configReader;
    private CtrfJsonComposer composer;

    @BeforeEach
    void setUp() {
        configReader = Mockito.mock(ConfigReader.class);
        composer = new CtrfJsonComposer(configReader);

        Mockito.when(configReader.getJUnitVersion()).thenReturn("mockedJUnitVersion");
        Mockito.when(configReader.getReportName()).thenReturn("mockedReportName");
        Mockito.when(configReader.getAppName()).thenReturn("mockedAppName");
        Mockito.when(configReader.getAppVersion()).thenReturn("mockedAppVersion");
        Mockito.when(configReader.getBuildName()).thenReturn("mockedBuildName");
        Mockito.when(configReader.getBuildNumber()).thenReturn("mockedBuildNumber");
        Mockito.when(configReader.getBuildUrl()).thenReturn("mockedBuildUrl");
        Mockito.when(configReader.getRepositoryName()).thenReturn("mockedRepositoryName");
        Mockito.when(configReader.getRepositoryUrl()).thenReturn("mockedRepositoryUrl");
        Mockito.when(configReader.getCommit()).thenReturn("mockedCommit");
        Mockito.when(configReader.getBranchName()).thenReturn("mockedBranchName");
        Mockito.when(configReader.getOsPlatform()).thenReturn("mockedOsPlatform");
        Mockito.when(configReader.getOsRelease()).thenReturn("mockedOsRelease");
        Mockito.when(configReader.getOsVersion()).thenReturn("mockedOsVersion");
        Mockito.when(configReader.getTestEnvironment()).thenReturn("mockedTestEnvironment");
    }

    @org.junit.jupiter.api.Test
    void testGenerateCtrfJsonWithValidInput() {
        var summary = Summary.builder().build();
        var tests = List.of(Test.builder().build(), Test.builder().build());

        var result = composer.generateCtrfJson(summary, tests);

        assertNotNull(result);
        assertNotNull(result.getResults());
        assertEquals(tests, result.getResults().getTests());
        assertEquals(summary, result.getResults().getSummary());

        var tool = result.getResults().getTool();
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

    @org.junit.jupiter.api.Test
    void testGenerateCtrfJsonWithEmptyTestList() {
        var summary = Summary.builder().build();
        List<Test> tests = Collections.emptyList();

        var result = composer.generateCtrfJson(summary, tests);

        assertNotNull(result);
        assertNotNull(result.getResults());
        assertEquals(tests, result.getResults().getTests());
        assertEquals(summary, result.getResults().getSummary());
    }

    @org.junit.jupiter.api.Test
    void testGenerateCtrfJsonWithNullSummary() {
        var tests = List.of(Test.builder().build());

        var result = composer.generateCtrfJson(null, tests);

        assertNotNull(result);
        assertNotNull(result.getResults());
        assertEquals(tests, result.getResults().getTests());
        assertNull(result.getResults().getSummary());
    }
}
