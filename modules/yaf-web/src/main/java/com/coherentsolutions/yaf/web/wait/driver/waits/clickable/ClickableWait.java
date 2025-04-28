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

package com.coherentsolutions.yaf.web.wait.driver.waits.clickable;

import com.coherentsolutions.yaf.web.wait.driver.BaseWait;
import com.coherentsolutions.yaf.web.wait.driver.CustomExpectedConditions;
import com.coherentsolutions.yaf.web.wait.driver.WaitConsts;
import com.coherentsolutions.yaf.web.wait.driver.WaitFor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This class provides custom wait conditions for checking the attributes of web elements.
 * <p>
 * The class extends the `BaseWait` class and provides functionality to wait for specific
 * attributes of web elements to be present or absent, with options for exact matches or
 * partial matches.
 * </p>
 * <p>
 * The class supports the use of annotations to configure the wait conditions and can be
 * used with Selenium WebDriver to facilitate more complex wait conditions that are not
 * provided by default.
 * </p>
 */
@Setter
@Accessors(fluent = true)
public class ClickableWait extends BaseWait {

    /**
     * The Clickable.
     */
    boolean clickable = true;

    /**
     * Instantiates a new Clickable wait.
     *
     * @param element    the element
     * @param waitConsts the wait consts
     */
    public ClickableWait(WebElement element, WaitConsts... waitConsts) {
        super(element, waitConsts);
    }

    /**
     * Instantiates a new Clickable wait.
     *
     * @param by         the by
     * @param waitConsts the wait consts
     */
    public ClickableWait(By by, WaitConsts... waitConsts) {
        super(by, waitConsts);
    }

    /**
     * Instantiates a new Clickable wait.
     *
     * @param waitFor        the wait for
     * @param waitAnnotation the wait annotation
     * @param elements       the elements
     */
    public ClickableWait(WaitFor waitFor, Annotation waitAnnotation, List<WebElement> elements) {
        super(waitFor, waitAnnotation, elements);
    }

    @Override
    protected void processCustomWaitAnnotation() {
        try {
            WaitForClickable waitForClickable = (WaitForClickable) waitAnnotation;
            clickable = waitForClickable.clickable();
        } catch (ClassCastException ex) {
            // to support default behaviour for waitFor annotation
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <R> R wait(WebDriverWait wait) {
        if (getElements() == null || getElements().isEmpty()) {
            if (clickable) {
                return (R) wait
                        .ignoring(StaleElementReferenceException.class)
                        .until(CustomExpectedConditions.elementsToBeClickableForLocators(getLocators()));
            } else {
                return (R) wait
                        .ignoring(StaleElementReferenceException.class)
                        .until(ExpectedConditions.not(CustomExpectedConditions.elementsToBeClickableForLocators(getLocators())));
            }
        } else {
            if (clickable) {
                return (R) wait
                        .ignoring(StaleElementReferenceException.class)
                        .until(CustomExpectedConditions.elementsToBeClickableForWebElements(getElements()));
            } else {
                return (R) wait
                        .ignoring(StaleElementReferenceException.class)
                        .until(ExpectedConditions.not(CustomExpectedConditions.elementsToBeClickableForWebElements(getElements())));
            }
        }
    }
}
