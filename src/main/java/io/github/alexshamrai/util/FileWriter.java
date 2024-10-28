package io.github.alexshamrai.util;

import io.github.alexshamrai.ctrf.model.CtrfJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class FileWriter {

    private static final String DEFAULT_REPORT_PATH = "ctrf-report.json";
    private static final String REPORT_PATH_PROPERTY = "ctrf.report.path";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void writeResultsToFile(CtrfJson ctrfJson) {
        String filePath = ConfigReader.getProperty(REPORT_PATH_PROPERTY, DEFAULT_REPORT_PATH);
        try {
            objectMapper.writeValue(new File(filePath), ctrfJson);
        } catch (IOException e) {
            //TODO: add logging, consider get rid of checked exception
            throw new RuntimeException("Failed to write results to file: " + filePath, e);
        }
    }
}