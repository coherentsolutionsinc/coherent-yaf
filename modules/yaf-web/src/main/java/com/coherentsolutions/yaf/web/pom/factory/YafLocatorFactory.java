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


import com.coherentsolutions.yaf.web.pom.WebComponent;
import com.coherentsolutions.yaf.web.pom.by.YafSetParam;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;
import com.coherentsolutions.yaf.web.utils.by.YafBy;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * The type Yaf locator factory.
 */
public abstract class YafLocatorFactory implements ElementLocatorFactory {

    /**
     * The Web driver.
     */
    @Setter
    protected WebDriver webDriver;

    /**
     * The Root.
     */
    @Setter
    protected WebElement root;

    /**
     * The Parent root.
     */
    @Setter
    protected WebElement parentRoot;

    /**
     * The Parent.
     */
    @Setter
    protected WebComponent parent;

    /**
     * The Parent yaf by.
     */
    @Setter
    protected YafBy parentYafBy;

    /**
     * The Annotation map.
     */
    @Setter
    protected Map<String, YafSetSelector> annotationMap;

    /**
     * The Yaf set param.
     */
    @Setter
    protected List<YafSetParam> yafSetParam;

    public abstract ElementLocator createLocator(Field field);
}