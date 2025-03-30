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

package com.coherentsolutions.yaf.web.pom.component;


import com.coherentsolutions.yaf.web.pom.RootedWebComponent;
import com.coherentsolutions.yaf.web.pom.WebComponent;
import com.coherentsolutions.yaf.web.pom.WebPage;
import com.coherentsolutions.yaf.web.utils.by.YafBy;
import com.coherentsolutions.yaf.web.utils.component.WebComponentBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * The interface Yaf web component.
 *
 * @param <T> the type parameter
 */
public interface YafWebComponent<T extends WebElement> extends RootedWebComponent<T> {

    /**
     * Gets web driver.
     *
     * @return the web driver
     */
    WebDriver getWebDriver();

    /**
     * Find element t.
     *
     * @param by the by
     * @return the t
     */
    default T findElement(YafBy by) {
        return findNestedElement(null, by);
    }

    /**
     * Find elements list.
     *
     * @param by the by
     * @return the list
     */
    default List<T> findElements(YafBy by) {
        return findNestedElements(null, by);
    }

    /**
     * Find nested element t.
     *
     * @param element the element
     * @param by      the by
     * @return the t
     */
    default T findNestedElement(T element, YafBy by) {
        if (element == null) {
            return (T) getWebDriver().findElement(by);
        }
        return (T) element.findElement(by);
    }

    /**
     * Find nested elements list.
     *
     * @param element the element
     * @param by      the by
     * @return the list
     */
    default List<T> findNestedElements(T element, YafBy by) {
        if (element == null) {
            return (List<T>) getWebDriver().findElements(by);
        }
        return (List<T>) element.findElements(by);
    }

    /**
     * Creates a new `WebComponentBuilder` for the specified component class.
     * <p>
     * This method initializes a `WebComponentBuilder` for the given component class,
     * allowing for the construction and configuration of web components.
     * </p>
     *
     * @param <C> the type of the web component
     * @param componentClass the class of the web component to be built
     * @return a new `WebComponentBuilder` for the specified component class
     */
    <C extends WebComponent<T>> WebComponentBuilder<C, T> createWebComponentBuilder(Class<C> componentClass);

    /**
     * Gets page.
     *
     * @param <P> the type parameter
     * @param cls the cls
     * @return the page
     */
    <P extends WebPage> P getPage(Class cls);
}
