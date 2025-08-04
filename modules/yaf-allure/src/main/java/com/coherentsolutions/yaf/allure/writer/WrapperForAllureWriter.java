package com.coherentsolutions.yaf.allure.writer;

import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.FileSystemResultsWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Wrapper for Allure writter to delete files before placing new ones
 * Original Writter throws exception if file exists.
 */
@AllArgsConstructor
public class WrapperForAllureWriter {

    AllureResultsWriter allureWriter;

    /**
     * Method to add a file to test-results with deletion if it already exists
     * @param filename output fileName
     * @param bytes output content
     */
    public void write(String filename, byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            removeResultFile(filename);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            allureWriter.write(filename, inputStream);
        }
    }

    /**
     * Get Path to allure results
     * @return Path to allure results directory
     */
    @SneakyThrows
    private Path getDefaultPath() {
        FileSystemResultsWriter writer = (FileSystemResultsWriter) allureWriter;
        Field oD = ReflectionUtils.findField(FileSystemResultsWriter.class, "outputDirectory");
        Objects.requireNonNull(oD).setAccessible(true);
        return (Path) oD.get(writer);
    }

    /**
     * remove file if it exists
     * @param fileName name of the file for removing
     */
    @SneakyThrows
    private void removeResultFile(String fileName) {
        Path outputDir = getDefaultPath();
        Path targetFile = outputDir.resolve(fileName);
        Files.deleteIfExists(targetFile);
    }
}