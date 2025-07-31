package com.coherentsolutions.yaf.allure;

import io.qameta.allure.util.PropertiesUtils;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class AllureCategoriesUtil {
    /**
     * Copies a categories.json file into the allure-results folder.
     *
     * @param sourcePath Path to categories.json (can be classpath resource or filesystem path)
     */
    @SneakyThrows
    public static void copyCategoriesJsonToAllureResults(String sourcePath) {
        if (sourcePath == null || sourcePath.isEmpty()) {
            return;
        }
        final Properties properties = PropertiesUtils.loadAllureProperties();
        final String path = properties.getProperty("allure.results.directory", "allure-results");
        Path targetDir = Paths.get(path);
        Path targetFile = targetDir.resolve("categories.json");
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        InputStream inputStream = AllureCategoriesUtil.class.getClassLoader().getResourceAsStream(sourcePath);
        if (inputStream == null) {
            Path sourceFile = Paths.get(sourcePath);
            if (!Files.exists(sourceFile)) {
                throw new IllegalArgumentException("Could not find categories.json at: " + sourcePath);
            }
            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}