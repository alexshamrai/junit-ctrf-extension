package io.github.alexshamrai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alexshamrai.ctrf.model.CtrfJson;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.github.alexshamrai.config.ConfigReader.getReportPath;

@RequiredArgsConstructor
public class FileWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void writeResultsToFile(CtrfJson ctrfJson) {
        var filePath = getReportPath();
        var path = Paths.get(filePath);
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            if (Files.exists(path)) {
                System.err.println("File already exists: " + filePath);
                return;
            }

            objectMapper.writeValue(path.toFile(), ctrfJson);
        } catch (AccessDeniedException e) {
            System.err.println("Access denied: " + filePath + " - " + e.getMessage());
        } catch (FileAlreadyExistsException e) {
            System.err.println("File already exists: " + filePath + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Failed to write results to file: " + filePath + " - " + e.getMessage());
        }
    }
}