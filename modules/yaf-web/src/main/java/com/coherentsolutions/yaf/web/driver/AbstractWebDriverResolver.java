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

import com.coherentsolutions.yaf.core.drivers.manager.DriverResolver;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.drivers.properties.GeneralDriverProperties;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.exec.model.device.BrowserDevice;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.web.driver.holder.WebDriverHolder;
import com.coherentsolutions.yaf.web.processor.log.driver.DriverLogProcessor;
import com.coherentsolutions.yaf.web.wait.driver.WaitProperties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * The type Abstract web driver resolver.
 */
@Component
public abstract class AbstractWebDriverResolver extends DriverResolver {

    private static final String HEADLESS = "-headless"; //TODO 112
    /**
     * The Properties.
     */
    @Autowired
    protected WebDriverProperties properties;
    /**
     * The Logging manager.
     */
    @Autowired(required = false)
    protected DriverLogProcessor loggingManager;
    /**
     * The Wait properties.
     */
    @Autowired
    protected WaitProperties waitProperties;
    /**
     * The Driver properties.
     */
    @Autowired
    GeneralDriverProperties driverProperties;

//    @Autowired(required = false)
//    WebDriverListener webDriverListener;

    @Override
    public DeviceType getResolverType() {
        return DeviceType.WEB;
    }

    /**
     * Build chrome driver chrome driver.
     *
     * @param device the device
     * @return the chrome driver
     */
    protected ChromeDriver buildChromeDriver(BrowserDevice device) {
        ChromeOptions chromeOptions = getChromeOptions(device);
        return new ChromeDriver(chromeOptions);
    }

    protected ChromeOptions getChromeOptions (BrowserDevice device) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions = chromeOptions.merge(buildCapabilitiesFromEnv(device));
        if (properties.isHeadless()) {
            chromeOptions.addArguments("--headless=new");
        }
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-notifications");
        List<String> args = device.getArgs();
        if (args != null && !args.isEmpty()) {
            chromeOptions.addArguments(args);
        }
        return chromeOptions;
    }

    /**
     * Build fire fox driver firefox driver.
     *
     * @param device the device
     * @return the firefox driver
     */
    protected FirefoxDriver buildFireFoxDriver(BrowserDevice device) {
        FirefoxOptions firefoxOptions = getFirefoxOptions(device);
        return new FirefoxDriver(firefoxOptions);
    }

    protected FirefoxOptions getFirefoxOptions (BrowserDevice device) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions = firefoxOptions.merge(buildCapabilitiesFromEnv(device));
        if (properties.isHeadless()) {
            firefoxOptions.addArguments(HEADLESS);
        }
        List<String> args = device.getArgs();
        if (args != null && !args.isEmpty()) {
            firefoxOptions.addArguments(args);
        }
        return firefoxOptions;
    }

    /**
     * Build safari driver safari driver.
     *
     * @param device the device
     * @return the safari driver
     */
    protected SafariDriver buildSafariDriver(BrowserDevice device) {
        SafariOptions safariOptions = getSafariOptions(device);
        return new SafariDriver(safariOptions);
    }

    protected SafariOptions getSafariOptions (BrowserDevice device){
        SafariOptions safariOptions = new SafariOptions();
        safariOptions = safariOptions.merge(buildCapabilitiesFromEnv(device));
        return safariOptions;
    }

    /**
     * Build edge driver edge driver.
     *
     * @param device the device
     * @return the edge driver
     */
    protected EdgeDriver buildEdgeDriver(BrowserDevice device) {
        EdgeOptions edgeOptions = getEdgeOptions(device);
        return new EdgeDriver(edgeOptions);
    }

    protected EdgeOptions getEdgeOptions (BrowserDevice device){
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions = edgeOptions.merge(buildCapabilitiesFromEnv(device));
        if (properties.isHeadless()) {
            edgeOptions.addArguments("--headless=new");
        }
        List<String> args = device.getArgs();
        if (args != null && !args.isEmpty()) {
            edgeOptions.addArguments(args);
        }
        return edgeOptions;
    }

    /**
     * Build ie driver internet explorer driver.
     *
     * @param device the device
     * @return the internet explorer driver
     */
    protected InternetExplorerDriver buildIEDriver(BrowserDevice device) {
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions = ieOptions.merge(buildCapabilitiesFromEnv(device));
        return new InternetExplorerDriver(ieOptions);
    }

    @Override
    public DriverHolder initDriver(Device device) {
        WebDriverHolder holder = getDriverHolder(device);
        if (properties.isMaximizeBrowser()) {
            holder.getDriver().manage().window().maximize();
        }
        setImplicitWaits(holder.getDriver());
//        if (driverProperties.isPublishDriverEvents()) {
//                    EventFiringWebDriver efwd = new EventFiringWebDriver(holder.getDriver());
//                    efwd.register(webDriverListener);
//                    holder.setDriver(efwd); //TODO 112 sseems that not supported for the new version
//        }
        return holder;
    }

    /**
     * Build capabilities from env org . openqa . selenium . capabilities.
     *
     * @param device the device
     * @return the org . openqa . selenium . capabilities
     */
    protected org.openqa.selenium.Capabilities buildCapabilitiesFromEnv(Device device) {
        org.openqa.selenium.remote.DesiredCapabilities capabilities = new org.openqa.selenium.remote.DesiredCapabilities(device.getCapabilities());
        if (loggingManager != null) {
            org.openqa.selenium.logging.LoggingPreferences loggingPreferences = loggingManager.getLoggingConfig();
            if (loggingPreferences != null) {
                capabilities.setCapability("goog:loggingPrefs", loggingPreferences);
            }
        }
        return capabilities;
    }

    /**
     * Sets implicit waits.
     *
     * @param driver the driver
     */
    protected void setImplicitWaits(WebDriver driver) {
        WaitProperties.ImplicitProperties implicit = waitProperties.getImplicit();
        if (implicit.getTimeOut() != null) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit.getTimeOut()));
        }
        if (implicit.getPageLoad() != null) {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(implicit.getPageLoad()));
        }
        if (implicit.getScript() != null) {
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(implicit.getScript()));
        }
    }

    /**
     * Gets driver holder.
     *
     * @param device the device
     * @return the driver holder
     */
    protected abstract WebDriverHolder getDriverHolder(Device device);
}
