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

package com.coherentsolutions.yaf.web.wait.driver;

import com.coherentsolutions.yaf.core.enums.DeviceType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Base wait.
 */
@Data
@Accessors(chain = true)
public abstract class BaseWait {

    /**
     * The Wait annotation.
     */
    protected Annotation waitAnnotation;
    /**
     * The Wait for.
     */
    protected WaitFor waitFor;
    /**
     * The Assert exceptions list.
     */
    protected List<Class> assertExceptionsList;
    /**
     * The Retry exception list.
     */
    protected List<Class> retryExceptionList;
    /**
     * The Retry times.
     */
    protected int retryTimes;
    /**
     * The Retries.
     */
    protected AtomicInteger retries = new AtomicInteger(0);
    /**
     * The Device type.
     */
    DeviceType deviceType;
    /**
     * The Driver.
     */
    WebDriver driver;
    /**
     * The Locators.
     */
    List<By> locators;
    /**
     * The Elements.
     */
    List<WebElement> elements;
    /**
     * The Wait consts.
     */
    WaitConsts waitConsts;

    /**
     * Instantiates a new Base wait.
     *
     * @param waitFor        the wait for
     * @param waitAnnotation the wait annotation
     * @param elements       the elements
     */
    public BaseWait(WaitFor waitFor, Annotation waitAnnotation, List<WebElement> elements) {
        this.elements = elements;
        this.waitAnnotation = waitAnnotation;
        this.waitFor = waitFor;
        this.waitConsts = waitFor.waitConsts();
        this.assertExceptionsList = Arrays.asList(waitFor.assertExceptionsList());
        this.retryExceptionList = Arrays.asList(waitFor.retryExceptionList());
        this.retryTimes = waitFor.retryTimes();
        processCustomWaitAnnotation();
    }

    /**
     * Instantiates a new Base wait.
     *
     * @param element the element
     */
    public BaseWait(WebElement element) {
        this.elements = Collections.singletonList(element);
    }

    /**
     * Instantiates a new Base wait.
     *
     * @param by the by
     */
    public BaseWait(By by) {
        this.locators = Collections.singletonList(by);
    }

    /**
     * Instantiates a new Base wait.
     *
     * @param by         the by
     * @param waitConsts the wait consts
     */
    public BaseWait(By by, WaitConsts... waitConsts) {
        this.locators = Collections.singletonList(by);
        if (waitConsts.length > 0) {
            this.waitConsts = waitConsts[0];
        }
    }

    /**
     * Instantiates a new Base wait.
     *
     * @param element    the element
     * @param waitConsts the wait consts
     */
    public BaseWait(WebElement element, WaitConsts... waitConsts) {
        this.elements = Collections.singletonList(element);
        if (waitConsts.length > 0) {
            this.waitConsts = waitConsts[0];
        }
    }

    /**
     * With locator base wait.
     *
     * @param locator the locator
     * @return the base wait
     */
    public BaseWait withLocator(By locator) {
        if (this.locators == null) {
            this.locators = new ArrayList<>();
        }
        this.locators.add(locator);
        return this;
    }

    /**
     * With element base wait.
     *
     * @param element the element
     * @return the base wait
     */
    public BaseWait withElement(WebElement element) {
        if (this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(element);
        return this;
    }

    /**
     * Can retry boolean.
     *
     * @param ex the ex
     * @return the boolean
     */
    public boolean canRetry(Exception ex) {
        return retryExceptionList != null && retries.get() < retryTimes && retryExceptionList.contains(ex.getClass());
    }

    /**
     * Should throw assert error boolean.
     *
     * @param ex the ex
     * @return the boolean
     */
    public boolean shouldThrowAssertError(Exception ex) {
        return assertExceptionsList != null && assertExceptionsList.contains(ex.getClass());
    }

    /**
     * Process custom wait annotation.
     */
// Add more assertExceptionsList or retries if you want to make them dynamic
    protected abstract void processCustomWaitAnnotation();

    /**
     * Wait with driver wait r.
     *
     * @param <R>           the type parameter
     * @param webDriverWait the web driver wait
     * @return the r
     */
    <R> R waitWithDriverWait(WebDriverWait webDriverWait) {
        return wait(webDriverWait);
    }

    /**
     * Wait r.
     *
     * @param <R>  the type parameter
     * @param wait the wait
     * @return the r
     */
    protected abstract <R> R wait(WebDriverWait wait);

}
