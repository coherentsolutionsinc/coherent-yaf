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

import com.coherentsolutions.yaf.core.exception.DriverYafException;
import com.coherentsolutions.yaf.core.exec.model.device.BrowserDevice;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.exec.model.farm.Farm;
import com.coherentsolutions.yaf.web.driver.holder.WebDriverHolder;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import static com.coherentsolutions.yaf.core.consts.Consts.DEFAULT_REMOTE_FARM;

/**
 * The type Base remote web driver resolver.
 */
@Service
@Slf4j
public class BaseRemoteWebDriverResolver extends AbstractWebDriverResolver {
	@Override
	public boolean canResolve(Device device) {
		return super.canResolve(device) && farmName(device, DEFAULT_REMOTE_FARM);
	}

	@Override
	protected WebDriverHolder getDriverHolder(Device device) {
		BrowserDevice browserDevice = (BrowserDevice) device;
		MutableCapabilities caps;
		log.debug("------ BUILD Remote DRIVER --------- ");
		Farm farm = device.getFarm();
		if (farm == null) {
			throw new DriverYafException(
					"Please specify farm for device " + device.getName() + " to use Remote WebDriver!");
		}
		String url = farm.getUrl();
		if (url.contains("%user%")) {
			url = url.replace("%user%", farm.getUser());
		}
		if (url.contains("%key%")) {
			url = url.replace("%key%", farm.getKey());
		}
		log.trace(url);

		switch (browserDevice.getBrowser()) {
			case CHROME: {
				caps = getChromeOptions(browserDevice);
				break;
			}
			case FF: {
				caps = getFirefoxOptions(browserDevice);
				break;
			}
			case SAFARI: {
				caps = getSafariOptions(browserDevice);
				break;
			}
			case EDGE: {
				caps = getEdgeOptions(browserDevice);
				break;
			}
			default: {
				throw new DriverYafException("Unknown browser type!");
			}
		}
		try {
			RemoteWebDriver driver = new RemoteWebDriver(new URL(url), caps);
			driver.setFileDetector(new LocalFileDetector());
			return new WebDriverHolder(device, properties, driver);
		} catch (MalformedURLException e) {
			throw new DriverYafException("Unable to create Remote driver", e);
		}
	}
}