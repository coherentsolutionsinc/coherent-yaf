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

package com.coherentsolutions.yaf.web.utils.by;


import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Yaf device by.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class YafDeviceBy extends By {

    /**
     * The Selectors.
     */
    Map<DeviceType, YafBy> selectors;
    /**
     * The Test execution context.
     */
    TestExecutionContext testExecutionContext;
    /**
     * The Driver holder.
     */
    DriverHolder driverHolder;

    /**
     * Instantiates a new Yaf device by.
     *
     * @param testExecutionContext the test execution context
     * @param driverHolder         the driver holder
     */
    public YafDeviceBy(TestExecutionContext testExecutionContext, DriverHolder driverHolder) {
        this.testExecutionContext = testExecutionContext;
        this.driverHolder = driverHolder;
        selectors = new LinkedHashMap<>();
    }

    private By findByAccordingEnv() throws GeneralYafException {
        // TODO double check in parallel execution
        By by = selectors.get(driverHolder.getDevice().getType()); // TODO not found!!!
        return by;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        try {
            return context.findElements(findByAccordingEnv());
        } catch (GeneralYafException ex) {
            // TODO xx
            throw new WebDriverException("Could not find elements via selector " + this);
        }
    }

    @Override
    public WebElement findElement(SearchContext context) {
        try {
            return context.findElement(findByAccordingEnv());
        } catch (GeneralYafException ex) {
            // TODO xx
            throw new WebDriverException("Could not find elements via selector " + this);
        }
    }

    /**
     * Add yaf device by.
     *
     * @param device the device
     * @param by     the by
     * @return the yaf device by
     */
    public YafDeviceBy add(DeviceType device, YafBy by) {
        selectors.put(device, by);
        return this;
    }
}
