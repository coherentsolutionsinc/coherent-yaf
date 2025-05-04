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

package com.coherentsolutions.yaf.web.driver.holder;

import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.drivers.properties.DriverProperties;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * The type Web driver holder.
 */
@Slf4j
public class WebDriverHolder extends DriverHolder<WebDriver> {
    /**
     * Instantiates a new Web driver holder.
     *
     * @param device     the device
     * @param properties the properties
     */
    public WebDriverHolder(Device device, DriverProperties properties) {
        super(device, properties);
    }

    /**
     * Instantiates a new Web driver holder.
     *
     * @param device     the device
     * @param properties the properties
     * @param driver     the driver
     */
    public WebDriverHolder(Device device, DriverProperties properties, WebDriver driver) {
        super(device, properties);
        this.driver = driver;
    }

    @Override
    public void quit() {
        try {
            log.info("Closing webdriver for {}", Thread.currentThread().getName());
            driver.close();
            driver.quit();
        } catch (Exception ex) {
            log.error("Unable to release driver " + driver + ". Cause " + ex.getMessage());
        }
    }
}
