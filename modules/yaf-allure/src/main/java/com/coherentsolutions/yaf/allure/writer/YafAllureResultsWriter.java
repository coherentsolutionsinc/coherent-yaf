package com.coherentsolutions.yaf.allure.writer;

import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.FileSystemResultsWriter;
import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The interface Yaf allure results writer.
 */
public interface YafAllureResultsWriter {

    /**
     * Write to results.
     *
     * @param writer the writer
     */
    void writeToResults(AllureResultsWriter writer);

    @SneakyThrows
    default Path getDefaultPath(AllureResultsWriter allureResultsWriter) {
        FileSystemResultsWriter writer = (FileSystemResultsWriter) allureResultsWriter;
        Field oD = ReflectionUtils.findField(FileSystemResultsWriter.class, "outputDirectory");
        oD.setAccessible(true);
        return (Path)oD.get(writer);
    }

    @SneakyThrows
    default void removeResultFile(AllureResultsWriter writer, String fileName) {
        Path outputDir = getDefaultPath(writer);
        Path targetFile = outputDir.resolve(fileName);
        Files.deleteIfExists(targetFile);
    }
}
