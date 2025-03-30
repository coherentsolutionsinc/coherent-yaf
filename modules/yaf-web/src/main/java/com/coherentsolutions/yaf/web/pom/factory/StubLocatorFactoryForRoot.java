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

package com.coherentsolutions.yaf.web.pom.factory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

import static com.coherentsolutions.yaf.web.pom.factory.DefaultYafLocatorFactory.buildFromYafBy;

/**
 * The type Stub locator factory for root.
 */
public class StubLocatorFactoryForRoot implements ElementLocatorFactory {

    /**
     * The Web driver.
     */
    WebDriver webDriver;
    /**
     * The By.
     */
    By by;

    /**
     * Instantiates a new Stub locator factory for root.
     *
     * @param webDriver the web driver
     * @param by        the by
     */
    public StubLocatorFactoryForRoot(WebDriver webDriver, By by) {
        this.webDriver = webDriver;
        this.by = by;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        return new DefaultElementLocator(webDriver, buildFromYafBy(by));
    }
}
