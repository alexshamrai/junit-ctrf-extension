package io.github.alexshamrai;

import io.github.alexshamrai.ctrf.model.Results;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alexshamrai.util.ConfigReader;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class FileWriter {

    private static final String DEFAULT_REPORT_PATH = "ctrf-report.json";
    private static final String REPORT_PATH_PROPERTY = "ctrf.report.path";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void writeResultsToFile(Results results) {
        String filePath = ConfigReader.getProperty(REPORT_PATH_PROPERTY, DEFAULT_REPORT_PATH);
        try {
            objectMapper.writeValue(new File(filePath), results);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write results to file: " + filePath, e);
        }
    }
}