/*
 * MIT License
 *
 * Copyright (c) 2021 - 2024 Coherent Solutions Inc.
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

package com.coherentsolutions.yaf.web.processor.log.driver;


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.log.TestLogData;
import com.coherentsolutions.yaf.core.log.properties.DriverLogProperties;
import com.coherentsolutions.yaf.core.log.properties.TestFinishProperties;
import com.coherentsolutions.yaf.core.processor.TestFinishEventProcessor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Driver log processor.
 */
@Component
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".processor.driver_log.enabled", havingValue = "true")
public class DriverLogProcessor implements TestFinishEventProcessor {

    /**
     * The Properties.
     */
    @Autowired
    TestFinishProperties properties;

    /**
     * Gets logging config.
     *
     * @return the logging config
     */
    @Cacheable("logProperties")
    public LoggingPreferences getLoggingConfig() {
        DriverLogProperties p = properties.getDriverLog();
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, p.getBrowser());
        logs.enable(LogType.CLIENT, p.getClient());
        logs.enable(LogType.DRIVER, p.getDriver());
        logs.enable(LogType.PERFORMANCE, p.getPerformance());
        logs.enable(LogType.SERVER, p.getServer());
        return logs;
    }

    /**
     * Grab all available log and add to finish event
     *
     * @param event                event
     * @param testExecutionContext context
     * @return list of testLogData
     */
    @Override
    public List<TestLogData> processFinishEvent(TestFinishEvent event, TestExecutionContext testExecutionContext) {
        return testExecutionContext.getUsedInTestDrivers().stream().filter(d -> d.isWeb()).flatMap(d -> {
            Logs logs = ((WebDriver) d.getDriver()).manage().logs();
            return logs.getAvailableLogTypes().stream().map(type -> {
                StringBuilder stringBuilder = new StringBuilder();
                logs.get(type).forEach(logEntry -> stringBuilder.append(logEntry.toString()));
                return new DriverLog(type, stringBuilder.toString());
            });
        }).collect(Collectors.toList());
    }
}
