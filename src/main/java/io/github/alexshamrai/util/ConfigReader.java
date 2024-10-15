package io.github.alexshamrai.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final String PROPERTIES_FILE = "ctrf.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("Properties file not found: " + PROPERTIES_FILE);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties file: " + PROPERTIES_FILE, e);
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}