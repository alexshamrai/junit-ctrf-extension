package io.github.alexshamrai;

import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.config.CtrfConfig;
import io.github.alexshamrai.ctrf.model.CtrfJson;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

public class FileWriterTest {

    private ConfigReader configReader;
    private FileWriter fileWriter;
    private CtrfJson ctrfJson;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;
    private final String filePath = "ctrf.json";

    @BeforeEach
    void setup() {
        var customConfig = new HashMap<String, String>();
        customConfig.put("ctrf.report.path", filePath);
        var mockConfig = ConfigFactory.create(CtrfConfig.class, customConfig);
        configReader = new ConfigReader(mockConfig);
        fileWriter = new FileWriter(configReader);
        ctrfJson = new CtrfJson();
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
        System.setErr(originalErr);
    }

    @Test
    void shouldSuccessfullyWriteResultsToFile() throws IOException {
        fileWriter.writeResultsToFile(ctrfJson);

        var path = Paths.get(filePath);
        assertThat(Files.exists(path)).isTrue();
        assertThat(Files.readString(path)).isNotEmpty();
    }

    @Test
    void shouldOverwriteExistingFile() throws IOException {
        Files.createFile(Paths.get(filePath));
        var initialModifiedTime = Files.getLastModifiedTime(Paths.get(filePath)).toMillis();

        fileWriter.writeResultsToFile(ctrfJson);
        var newModifiedTime = Files.getLastModifiedTime(Paths.get(filePath)).toMillis();

        assertThat(initialModifiedTime).isNotEqualTo(newModifiedTime);
        assertThat(errContent.toString()).contains("File already exists: " + filePath);
    }

    @Test
    void shouldHandleAccessDeniedException() {
        var nestedFilePath = "nested/ctrf.json";
        configReader = createConfigReaderWithPath(nestedFilePath);
        fileWriter = new FileWriter(configReader);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(Paths.get("nested"))).thenThrow(new AccessDeniedException("nested"));
            mockedFiles.when(() -> Files.exists(any())).thenReturn(false);

            fileWriter.writeResultsToFile(ctrfJson);

            mockedFiles.verify(() -> Files.createDirectories(Paths.get("nested")), times(1));
            assertThat(errContent.toString()).contains("Access denied: " + nestedFilePath);
        }
    }

    @Test
    void shouldHandleIoException() {
        var nestedFilePath = "test-dir/ctrf.json";
        configReader = createConfigReaderWithPath(nestedFilePath);
        fileWriter = new FileWriter(configReader);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(Paths.get("test-dir"))).thenThrow(new IOException("IO error"));
            mockedFiles.when(() -> Files.exists(any())).thenReturn(false);

            fileWriter.writeResultsToFile(ctrfJson);

            mockedFiles.verify(() -> Files.createDirectories(Paths.get("test-dir")), times(1));
            assertThat(errContent.toString()).contains("Failed to write results to file: " + nestedFilePath);
        }
    }

    private ConfigReader createConfigReaderWithPath(String path) {
        var customConfig = new HashMap<String, String>();
        customConfig.put("ctrf.report.path", path);
        var mockConfig = ConfigFactory.create(CtrfConfig.class, customConfig);
        return new ConfigReader(mockConfig);
    }
}