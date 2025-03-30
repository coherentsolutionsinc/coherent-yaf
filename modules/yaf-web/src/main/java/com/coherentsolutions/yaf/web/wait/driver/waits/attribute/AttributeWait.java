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

package com.coherentsolutions.yaf.web.wait.driver.waits.attribute;

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
public class AttributeWait extends BaseWait {

    String attribute;
    String value;
    boolean exactMatch = true;
    boolean presence = true;

    public AttributeWait(WebElement element, WaitConsts... waitConsts) {
        super(element, waitConsts);
    }

    public AttributeWait(By locator, WaitConsts... waitConsts) {
        super(locator, waitConsts);
    }

    public AttributeWait(WaitFor waitFor, Annotation waitAnnotation, List<WebElement> elements) {
        super(waitFor, waitAnnotation, elements);
    }

    @Override
    protected void processCustomWaitAnnotation() {
        try {
            WaitForAttribute waitForVisible = (WaitForAttribute) waitAnnotation;
            exactMatch = waitForVisible.exactMatch();
            presence = waitForVisible.presence();
        } catch (ClassCastException ex) {
            // to support default behaviour for waitFor annotation
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <R> R wait(WebDriverWait wait) {
        if (presence) {
            if (getElements() == null || getElements().isEmpty()) {
                if (exactMatch) {
                    return (R) wait.until(CustomExpectedConditions.attributeToBeForLocators(getLocators(), attribute, value));
                } else {
                    return (R) wait.until(CustomExpectedConditions.attributeContainsForLocators(getLocators(), attribute, value));
                }
            } else {
                if (exactMatch) {
                    return (R) wait.until(CustomExpectedConditions.attributeToBeForWebElements(getElements(), attribute, value));
                } else {
                    return (R) wait.until(CustomExpectedConditions.attributeContainsForWebElements(getElements(), attribute, value));
                }
            }
        } else {
            if (getElements() == null || getElements().isEmpty()) {
                if (exactMatch) {
                    return (R) wait.until(ExpectedConditions.not(CustomExpectedConditions.attributeToBeForLocators(getLocators(), attribute, value)));
                } else {
                    return (R) wait.until(ExpectedConditions.not(CustomExpectedConditions.attributeContainsForLocators(getLocators(), attribute, value)));
                }
            } else {
                if (exactMatch) {
                    return (R) wait.until(ExpectedConditions.not(CustomExpectedConditions.attributeToBeForWebElements(getElements(), attribute, value)));
                } else {
                    return (R) wait.until(ExpectedConditions.not(CustomExpectedConditions.attributeContainsForWebElements(getElements(), attribute, value)));
                }
            }
        }
    }
}

