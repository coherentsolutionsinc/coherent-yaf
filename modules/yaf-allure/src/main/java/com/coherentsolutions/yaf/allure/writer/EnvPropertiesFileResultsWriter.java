package com.coherentsolutions.yaf.allure.writer;

import com.coherentsolutions.yaf.allure.AllureProperties;
import io.qameta.allure.AllureResultsWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * The type Env properties file results writer.
 */
@Service
@Slf4j
public class EnvPropertiesFileResultsWriter implements YafAllureResultsWriter {

    private final String FILE_NAME = "environment.properties";

    /**
     * The Allure properties.
     */
    @Autowired
    AllureProperties allureProperties;

    /**
     * Map to properties properties.
     *
     * @param map the map
     * @return the properties
     */
    protected static Properties mapToProperties(final Map<String, String> map) {
        Properties properties = new Properties();
        map.forEach((key, value) -> properties.setProperty(key, value));
        return properties;
    }

    /**
     * Properties to input stream
     *
     * @param properties the properties
     * @return the input stream
     * @throws Exception the exception
     */
    protected static InputStream propertiesToInputStream(final Properties properties) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        properties.store(outputStream, null);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @SneakyThrows
    @Override
    public void writeToResults(AllureResultsWriter writer) {
        if (allureProperties.getEnvMap() != null) {
            removeResultFile(writer, FILE_NAME);
            Properties properties = mapToProperties(allureProperties.getEnvMap());
            writer.write(FILE_NAME, propertiesToInputStream(properties));
        }
    }
}
