package io.github.alexshamrai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.ctrf.model.CtrfJson;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles writing the CTRF JSON report to a file on the filesystem.
 * <p>
 * This class takes care of creating necessary directories, handling file system errors,
 * and serializing the CTRF JSON object to a file. The target file path is determined
 * by the configuration provided through {@link ConfigReader}.
 */
@RequiredArgsConstructor
public class FileWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConfigReader configReader;

    /**
     * Writes the provided CTRF JSON object to a file.
     * <p>
     * The method handles directory creation if needed and logs errors to the standard error
     * if any issues occur during file writing.
     *
     * @param ctrfJson the CTRF JSON object to write to file
     */
    public void writeResultsToFile(CtrfJson ctrfJson) {
        var filePath = configReader.getReportPath();
        var path = Paths.get(filePath);
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            if (Files.exists(path)) {
                System.err.println("File already exists: " + filePath);
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
