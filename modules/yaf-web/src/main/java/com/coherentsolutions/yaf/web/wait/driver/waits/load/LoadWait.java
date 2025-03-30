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

package com.coherentsolutions.yaf.web.wait.driver.waits.load;

import com.coherentsolutions.yaf.web.wait.driver.BaseWaitWithoutCustomAnnotation;
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
 * A wait condition that waits for a specific attribute or text of a web element to be loaded.
 * <p>
 * This class extends {@link BaseWaitWithoutCustomAnnotation} and provides functionality to wait
 * for a specific attribute or text of a web element to stabilize, meaning its value has remained
 * the same for at least two consecutive checks.
 * </p>
 */
@Setter
@Accessors(fluent = true)
public class LoadWait extends BaseWaitWithoutCustomAnnotation {

    boolean forText;
    boolean forPosition;
    String forAttribute;

    public LoadWait(WaitFor waitFor, Annotation waitAnnotation, List<WebElement> elements) {
        super(waitFor, waitAnnotation, elements);
    }

    public LoadWait(By by, WaitConsts... waitConsts) {
        super(by, waitConsts);
    }

    public LoadWait(WebElement element, WaitConsts... waitConsts) {
        super(element, waitConsts);
    }

    @Override
    protected <R> R wait(WebDriverWait wait) {
        if (getElements() == null || getElements().isEmpty()) {
            if (forAttribute != null) {
                return (R) wait.until(CustomExpectedConditions.attributeIsLoaded(getLocators().get(0), forAttribute));
            } else if (forPosition) {
                return (R) wait.until(CustomExpectedConditions.positionIsLoadedForLocator(getLocators().get(0)));
            }
        } else {
            if (forText) {
                return (R) wait.until(CustomExpectedConditions.hadElementTextUpdated(getElements().get(0)));
            } else if (forPosition) {
                return (R) wait.until(CustomExpectedConditions.positionIsLoadedForWebElement(getElements().get(0)));
            } else if (forAttribute != null) {
                return (R) wait.until(CustomExpectedConditions.attributeIsLoaded(getElements().get(0), forAttribute));
            }
        }
        return null;
    }
}
