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

package com.coherentsolutions.yaf.web.driver.vendor;

/*-
 * #%L
 * Yaf Web Module
 * %%
 * Copyright (C) 2020 - 2021 CoherentSolutions
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.coherentsolutions.yaf.core.exception.DriverYafException;
import com.coherentsolutions.yaf.core.exec.model.device.BrowserDevice;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.exec.model.farm.Farm;
import com.coherentsolutions.yaf.web.driver.AbstractWebDriverResolver;
import com.coherentsolutions.yaf.web.driver.holder.WebDriverHolder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

import static com.coherentsolutions.yaf.core.consts.Consts.FARM_BROWSERSTACK;

/**
 * The type Browser stack web driver resolver.
 */
@Service
@Slf4j
public class BrowserStackWebDriverResolver extends AbstractWebDriverResolver {

    @Override
    public boolean canResolve(Device device) {
        return super.canResolve(device) && farmName(device, FARM_BROWSERSTACK);
    }

    @Override
    protected WebDriverHolder getDriverHolder(Device device) {
        log.debug("------ BUILD BS DRIVER --------- ");
        Farm farm = device.getFarm();
        if (farm == null) {
            throw new DriverYafException(
                    "Please specify farm for device " + device.getName() + " to use BrowserStack!");
        }
        String url = farm.getUrl() == null ? "https://%user%:%key%@hub-cloud.browserstack.com/wd/hub" : farm.getUrl();
        url = url.replace("%user%", farm.getUser());
        url = url.replace("%key%", farm.getKey());
        log.trace(url);

        BrowserDevice browserDevice = (BrowserDevice) device;
        DesiredCapabilities caps = new DesiredCapabilities(browserDevice.getCapabilities());
        try {
            WebDriver driver = new RemoteWebDriver(new URL(url), caps);
            return new WebDriverHolder(device, properties, driver);
        } catch (MalformedURLException e) {
            throw new DriverYafException("Unable to create BS driver", e);
        }
    }
}
