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

package com.coherentsolutions.yaf.web.processor.log.pagesource;


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.log.TestLogData;
import com.coherentsolutions.yaf.core.log.properties.TestFinishProperties;
import com.coherentsolutions.yaf.core.processor.TestFinishEventProcessor;
import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Web page source processor.
 */
@Data
@Component
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".processor.web.enabled", havingValue = "true")
public class WebPageSourceProcessor implements TestFinishEventProcessor {

    /**
     * The Properties.
     */
    @Autowired
    TestFinishProperties properties;

    @Override
    public List<TestLogData> processFinishEvent(TestFinishEvent event, TestExecutionContext testExecutionContext) {
        if (properties.getWeb().isStoreSource()) {
            DriverHolder driverHolder = testExecutionContext.findInitedDriverHolderByType(DeviceType.WEB);
            if (driverHolder != null) {
                WebDriver driver = (WebDriver) driverHolder.getDriver();
                return List.of(
                        new WebPageSourceLog(driver.getCurrentUrl(), driver.getTitle(), driver.getPageSource()));
            }
        }
        return null;
    }
}
