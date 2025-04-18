package io.github.alexshamrai.integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtility {

    private static final Logger log = LoggerFactory.getLogger(FileUtility.class);

    public static boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        boolean exists = Files.exists(path);
        if (!exists) {
            log.info("File not found at: {}", path.toAbsolutePath());
        }
        return exists;
    }

    public static String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!fileExists(filePath)) {
            throw new IOException("File not found: " + path.toAbsolutePath());
        }
        String content = Files.readString(path);
        log.debug("File content read from: {}", path.toAbsolutePath());
        return content;
    }
}
