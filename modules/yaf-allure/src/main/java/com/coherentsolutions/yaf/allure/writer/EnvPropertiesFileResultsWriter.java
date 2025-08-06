/*
 * MIT License
 *
 * Copyright (c) 2021 - 2025 Coherent Solutions Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
    public void writeToResults(WrapperForAllureWriter writer) {
        if (allureProperties.getEnvMap() != null) {
            Properties properties = mapToProperties(allureProperties.getEnvMap());
            StringWriter sw = new StringWriter();
            properties.store(sw, "");
            writer.write(FILE_NAME, sw.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
