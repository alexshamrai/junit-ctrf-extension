package io.github.alexshamrai.integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service class for file operations.
 */
public class FileSteps {
    private static final Logger log = LoggerFactory.getLogger(FileSteps.class);

    /**
     * Checks if a file exists at the specified path.
     *
     * @param filePath the path to check
     * @return true if the file exists, false otherwise
     */
    public boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        boolean exists = Files.exists(path);
        if (!exists) {
            log.info("File not found at: {}", path.toAbsolutePath());
        }
        return exists;
    }

    /**
     * Reads the content of a file as a string.
     *
     * @param filePath the path of the file to read
     * @return the content of the file as a string
     * @throws IOException if an I/O error occurs
     */
    public String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            log.info("File not found at: {}", path.toAbsolutePath());
            throw new IOException("File not found: " + path.toAbsolutePath());
        }
        
        String content = Files.readString(path);
        log.debug("File content read from: {}", path.toAbsolutePath());
        return content;
    }

    /**
     * Gets the Path object for a file path.
     *
     * @param filePath the file path as a string
     * @return the Path object
     */
    public Path getPath(String filePath) {
        return Paths.get(filePath);
    }
}