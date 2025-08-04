package com.coherentsolutions.yaf.allure.writer;

import com.coherentsolutions.yaf.allure.AllureProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
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
        map.forEach(properties::setProperty);
        return properties;
    }

    @SneakyThrows
    @Override
    public void writeToResults(WrapperForAllureWriter writer) {
        if (allureProperties.getEnvMap() != null) {
            Properties properties = mapToProperties(allureProperties.getEnvMap());
            StringWriter sw = new StringWriter();
            properties.store(sw, "");
            writer.write(FILE_NAME, sw.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
