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

package com.coherentsolutions.yaf.web.wait.driver.waits.presence;

import com.coherentsolutions.yaf.web.wait.driver.BaseWait;
import com.coherentsolutions.yaf.web.wait.driver.CustomExpectedConditions;
import com.coherentsolutions.yaf.web.wait.driver.WaitConsts;
import com.coherentsolutions.yaf.web.wait.driver.WaitFor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This class provides custom wait conditions for checking the presence of web elements in the DOM.
 * <p>
 * The class extends the `BaseWait` class and provides functionality to wait for web elements
 * to be present or absent in the DOM, with options to handle any or all elements matching the criteria.
 * </p>
 * <p>
 * The class supports the use of annotations to configure the wait conditions and can be
 * used with Selenium WebDriver to facilitate more complex wait conditions that are not
 * provided by default.
 * </p>
 */
@Setter
@Accessors(fluent = true)
public class PresenceWait extends BaseWait {

    /**
     * The Presence.
     */
    boolean presence = true;
    /**
     * The Any element match.
     */
    boolean anyElementMatch;

    /**
     * Instantiates a new Presence wait.
     *
     * @param element    the element
     * @param waitConsts the wait consts
     */
    public PresenceWait(WebElement element, WaitConsts... waitConsts) {
        super(element, waitConsts);
    }

    /**
     * Instantiates a new Presence wait.
     *
     * @param by         the by
     * @param waitConsts the wait consts
     */
    public PresenceWait(By by, WaitConsts... waitConsts) {
        super(by, waitConsts);
    }

    /**
     * Instantiates a new Presence wait.
     *
     * @param waitFor        the wait for
     * @param waitAnnotation the wait annotation
     * @param elements       the elements
     */
    public PresenceWait(WaitFor waitFor, Annotation waitAnnotation, List<WebElement> elements) {
        super(waitFor, waitAnnotation, elements);
    }

    @Override
    protected void processCustomWaitAnnotation() {
        try {
            WaitForPresence waitForVisible = (WaitForPresence) waitAnnotation;
            presence = waitForVisible.presence();
            anyElementMatch = waitForVisible.anyElementMatch();
        } catch (ClassCastException ex) {
            // to support default behaviour for waitFor annotation
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <R> R wait(WebDriverWait wait) {
        if (getElements() == null || getElements().isEmpty()) {
            if (anyElementMatch) {
                if (presence) {
                    return (R) wait.until(CustomExpectedConditions.anyPresentInDomForLocators(getLocators()));
                } else {
                    return (R) wait.until(CustomExpectedConditions.anyRemovedFromDomForLocators(getLocators()));
                }
            } else {
                if (presence) {
                    return (R) wait.until(CustomExpectedConditions.allPresentInDomForLocators(getLocators()));
                } else {
                    return (R) wait.until(CustomExpectedConditions.allRemovedFromDomForLocators(getLocators()));
                }
            }
        } else {
            if (presence) {
                return (R) wait.until(CustomExpectedConditions.allPresentInDomForWebElements(getElements()));
            } else {
                return (R) wait.until(CustomExpectedConditions.allRemovedFromDomForWebElements(getElements()));
            }
        }
    }
}
