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

package com.coherentsolutions.yaf.web.pom;

import com.coherentsolutions.yaf.core.context.IContextual;
import com.coherentsolutions.yaf.core.pom.Component;
import com.coherentsolutions.yaf.web.driver.WebDriverProperties;
import com.coherentsolutions.yaf.web.pom.factory.YafLocatorFactory;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The type General web yaf object.
 */
class GeneralWebYafObject extends Component implements IContextual {

    /**
     * The Properties.
     */
    @Getter
    @Autowired
    protected WebDriverProperties properties;
    /**
     * The Locator factory.
     */
    @Getter
    @Autowired
    protected YafLocatorFactory locatorFactory;
//    /**
//     * The Wait service.
//     */
//    @Getter
//    @Autowired
//    protected DriverWaitService waitService;


    @Override
    protected void init(Field field, Object obj, Class fieldType, List<Annotation> annotations, String driverName) {
        driverHolder = testExecutionContext.getWebDriverHolder(driverName);
    }

    /**
     * Gets web driver.
     *
     * @return the web driver
     */
    public WebDriver getWebDriver() {
        return (WebDriver) driverHolder.getDriver();
    }
}
