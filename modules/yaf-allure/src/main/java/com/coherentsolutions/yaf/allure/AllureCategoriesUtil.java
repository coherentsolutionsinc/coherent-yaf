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

    private static final String DEFAULT_KNOWN_ISSUES_CATEGORY = "known-issues-category.json"; // in resources/

    /**
     * Copies custom category file.
     */
    @SneakyThrows
    public static void copyCustomCategoriesToAllureResults(String sourcePath) {
        copyCategoriesToAllureResults(sourcePath);
    }

    /**
     * Copies default category file.
     */
    @SneakyThrows
    public static void copyKnownIssuesCategoryToAllureResults() {
        copyCategoriesToAllureResults(DEFAULT_KNOWN_ISSUES_CATEGORY);
    }

    /**
     * Copies a categories.json file into the allure-results folder.
     *
     * @param sourcePath Path to categories.json (can be classpath resource or filesystem path)
     */
    @SneakyThrows
    private static void copyCategoriesToAllureResults(String sourcePath) {
        if (sourcePath == null || sourcePath.isEmpty()) {
            return;
        }
        Path targetFile = getCategoryTargetPath();
        InputStream inputStream = AllureCategoriesUtil.class.getClassLoader().getResourceAsStream(sourcePath);
        if (inputStream != null) {
            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        Path sourceFile = Paths.get(sourcePath);
        if (!Files.exists(sourceFile)) {
            throw new IllegalArgumentException("Could not find categories.json at: " + sourcePath);
        }

        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Prepare destination and get final path of target file for a categories.json file
     */
    @SneakyThrows
    private static Path getCategoryTargetPath() {
        final Properties properties = PropertiesUtils.loadAllureProperties();
        final String path = properties.getProperty("allure.results.directory", "allure-results");
        Path targetDir = Paths.get(path);
        Path targetFile = targetDir.resolve("categories.json");
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        return targetFile;
    }
}