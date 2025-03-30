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

package com.coherentsolutions.yaf.web.processor.log.screenshot;


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.log.ScreenshotLog;
import com.coherentsolutions.yaf.core.log.TestLogData;
import com.coherentsolutions.yaf.core.log.properties.ScreenshotProperties;
import com.coherentsolutions.yaf.core.log.properties.TestFinishProperties;
import com.coherentsolutions.yaf.core.processor.TestFinishEventProcessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.coherentsolutions.yaf.core.consts.Consts.PNG;

/**
 * The type Screenshot processor.
 */
@Data
@Service
@Slf4j
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".processor.screenshot.enabled", havingValue = "true")
public class ScreenshotProcessor implements TestFinishEventProcessor {

    /**
     * The Properties.
     */
    @Autowired
    TestFinishProperties properties;

    @Override
    public List<TestLogData> processFinishEvent(TestFinishEvent event, TestExecutionContext testExecutionContext) {
        return testExecutionContext.getUsedInTestDrivers().stream().map(d -> {
            WebDriver driver = (WebDriver) d.getDriver();
            Device device = d.getDevice();
            String name = device.getName();
            return new ScreenshotLog(name, getScreenBytes(device, driver));
        }).collect(Collectors.toList());
    }

    /**
     * Get screen bytes byte [ ].
     *
     * @param device the device
     * @param driver the driver
     * @return the byte [ ]
     */
    protected byte[] getScreenBytes(Device device, WebDriver driver) {
        ScreenshotProperties p = properties.getScreenshot();
        byte[] image = null;
        if (device.is(DeviceType.WEB)) {
            driver.switchTo().defaultContent();
            String script = "return window.devicePixelRatio;";
            Number ratio = (Number) ((JavascriptExecutor) driver).executeScript(script);
            Screenshot screenshot = new AShot()
                    .coordsProvider(new WebDriverCoordsProvider())
                    .shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(ratio.floatValue()), 200))
                    .takeScreenshot(driver);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(screenshot.getImage(), PNG, baos);
                image = baos.toByteArray();
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        } else {
            TakesScreenshot scrShot = (TakesScreenshot) driver;
            image = scrShot.getScreenshotAs(OutputType.BYTES);
        }
        if (p.isCompress()) {
            // TODO compress
        }
        return image;
    }
}
