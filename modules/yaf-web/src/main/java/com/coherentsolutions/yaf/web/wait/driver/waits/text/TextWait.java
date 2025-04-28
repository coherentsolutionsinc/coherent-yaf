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

package com.coherentsolutions.yaf.web.wait.driver.waits.text;

import com.coherentsolutions.yaf.web.wait.driver.BaseWait;
import com.coherentsolutions.yaf.web.wait.driver.CustomExpectedConditions;
import com.coherentsolutions.yaf.web.wait.driver.WaitConsts;
import com.coherentsolutions.yaf.web.wait.driver.WaitFor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * A wait condition that waits for the text of a web element to be updated or to match a specific value.
 * <p>
 * This class extends {@link BaseWait} and provides functionality to wait for the text of a web element
 * to stabilize, meaning its value has remained the same for at least two consecutive checks, or to match
 * a specific value.
 * </p>
 */
@Setter
@Accessors(fluent = true)
public class TextWait extends BaseWait {

    /**
     * The Should be equal.
     */
    boolean shouldBeEqual = true;
    /**
     * The Text.
     */
    String text;

    /**
     * Instantiates a new Text wait.
     *
     * @param element    the element
     * @param waitConsts the wait consts
     */
    public TextWait(WebElement element, WaitConsts... waitConsts) {
        super(element, waitConsts);
    }

    /**
     * Instantiates a new Text wait.
     *
     * @param by         the by
     * @param waitConsts the wait consts
     */
    public TextWait(By by, WaitConsts... waitConsts) {
        super(by, waitConsts);
    }

    /**
     * Instantiates a new Text wait.
     *
     * @param waitFor        the wait for
     * @param waitAnnotation the wait annotation
     * @param elements       the elements
     */
    public TextWait(WaitFor waitFor, Annotation waitAnnotation, List<WebElement> elements) {
        super(waitFor, waitAnnotation, elements);
    }

    @Override
    protected void processCustomWaitAnnotation() {
        try {
            WaitForText waitForVisible = (WaitForText) waitAnnotation;
            shouldBeEqual = waitForVisible.shouldBeEqual();
            text = waitForVisible.text();
        } catch (ClassCastException ex) {
            // to support default behaviour for waitFor annotation
        }
    }

    @Override
    protected <R> R wait(WebDriverWait wait) {
        if (getElements() == null || getElements().isEmpty()) {
            if (shouldBeEqual) {
                return (R) wait.until(ExpectedConditions.textToBe(getLocators().get(0), text));
            } else {
                return (R) wait.until(ExpectedConditions.textToBePresentInElementLocated(getLocators().get(0), text));
            }
        } else {
            if (shouldBeEqual) {
                return (R) wait.until(CustomExpectedConditions.textToBe(getElements().get(0), text));
            } else {
                return (R) wait.until(ExpectedConditions.textToBePresentInElement(getElements().get(0), text));
            }
        }
    }
}
