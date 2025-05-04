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

package com.coherentsolutions.yaf.web.driver;

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
import com.coherentsolutions.yaf.web.driver.holder.WebDriverHolder;
import com.coherentsolutions.yaf.web.utils.CapabilitiesUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * The type Base web driver resolver.
 */
@Service
@Order() // set this resolver at last position, cause other user defined should be prior that
@Slf4j
public class BaseWebDriverResolver extends AbstractWebDriverResolver {

    @Override
    public boolean canResolve(Device device) {
        return super.canResolve(device) && emptyFarm(device);
    }

    @Override
    protected WebDriverHolder getDriverHolder(Device device) {
        BrowserDevice browserDevice = (BrowserDevice) device;
        WebDriver driver = null;
        log.debug("Building web {} driver for env {}", browserDevice.getBrowser(), browserDevice.getName());
        switch (browserDevice.getBrowser()) {
            case CHROME: {
                WebDriverManager.chromedriver()
                        //.driverVersion("119.0.6045.105")
                        .setup();
                driver = buildChromeDriver(browserDevice);
                break;
            }
            case FF: {
                WebDriverManager.firefoxdriver().setup();
                driver = buildFireFoxDriver(browserDevice);
                break;
            }
            case SAFARI: {
                WebDriverManager.safaridriver().setup();
                driver = buildSafariDriver(browserDevice);
                break;
            }
            /*
             * case OPERA: { WebDriverManager.operadriver().setup(); driver = buildOperaDriver(browserDevice); break; }
             */
            case EDGE: {
                WebDriverManager.edgedriver().setup();
                driver = buildEdgeDriver(browserDevice);
                break;
            }
            case HTML_UNIT: {
                driver = new HtmlUnitDriver();//TODO think about somehow wrap get method and download html files to driver
                break;
            }
            default: {
                throw new DriverYafException("Unknown browser type!");
            }
        }
        String resolution = browserDevice.getResolution();
        if (resolution != null) {
            //TODO 112
            // TODO match this with condition matcher and other cloud farms
            driver.manage().window().setSize(CapabilitiesUtils.getDimensionFromResolution(resolution));
        }

        log.debug("****** CREATE NEW WEB DRIVER ******** ");
        return new WebDriverHolder(browserDevice, properties, driver);
    }

}
