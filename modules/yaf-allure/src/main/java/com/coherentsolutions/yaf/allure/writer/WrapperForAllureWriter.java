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

@AllArgsConstructor
public class WrapperForAllureWriter {

    AllureResultsWriter allureWriter;

    public void write(String filename, byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            removeResultFile(filename);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            allureWriter.write(filename, inputStream);
        }
    }

    @SneakyThrows
    private Path getDefaultPath() {
        FileSystemResultsWriter writer = (FileSystemResultsWriter) allureWriter;
        Field oD = ReflectionUtils.findField(FileSystemResultsWriter.class, "outputDirectory");
        oD.setAccessible(true);
        return (Path) oD.get(writer);
    }

    @SneakyThrows
    private void removeResultFile(String fileName) {
        Path outputDir = getDefaultPath();
        Path targetFile = outputDir.resolve(fileName);
        Files.deleteIfExists(targetFile);
    }
}
