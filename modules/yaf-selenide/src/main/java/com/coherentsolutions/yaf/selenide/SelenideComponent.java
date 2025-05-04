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

package com.coherentsolutions.yaf.selenide;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.coherentsolutions.yaf.web.pom.WebComponent;
import com.coherentsolutions.yaf.web.pom.component.YafWebComponent;
import com.coherentsolutions.yaf.web.utils.by.YafBy;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * The type Selenide component.
 */
public abstract class SelenideComponent extends WebComponent<SelenideElement> implements YafWebComponent<SelenideElement> { //TODO 112 implements YafWaitable {

    @Override
    public WebDriver getWebDriver() {
        return WebDriverRunner.getWebDriver();
    }

    @Override
    public SelenideElement findNestedElement(SelenideElement element, YafBy by) {
        if (element == null) {
            return $(by);
        }
        return element.find(by);
    }

    @Override
    public List<SelenideElement> findNestedElements(SelenideElement element, YafBy by) {
        if (element == null) {
            return StreamSupport.stream($$(by).spliterator(), false)
                    .collect(Collectors.toList());
        }
        return StreamSupport.stream(element.findAll(by).spliterator(), false)
                .collect(Collectors.toList());
    }

    //TODO 112
//    protected Map<YafBy, SelenideElement> cachedElements = new ConcurrentHashMap<>();
//
//    public abstract YafBy getElementSelector();
//
//    public abstract void setElementSelector(YafBy elementSelector);
//
//    protected SelenideElement getParent() {
//        return null;
//    }
//
//    public void setParent(SelenideElement parent) {
//    }
//
//    protected SelenideElement getElement() {
//        return getElement(getElementSelector());
//    }
//
//    protected SelenideElement getElement(YafBy yafBy) {
//        SelenideElement cachedElement = cachedElements.get(yafBy);
//        if (cachedElement == null) {
//            if (getParent() != null) {
//                cachedElement = getParent().find(yafBy);
//            } else {
//                cachedElement = $(yafBy);
//            }
//        }
//        return cachedElement;
//    }
//
//    protected String getElementText() {
//        return getElement(getElementSelector()).getText();
//    }
//
//    protected String getElementText(YafBy yafBy) {
//        return getElement(yafBy).getText();
//    }
//
//    protected void clickElement() {
//        getElement(getElementSelector()).click();
//    }
//
//    protected void clickElement(YafBy yafBy) {
//        getElement(yafBy).click();
//    }
//
//    protected SelenideElement getEnrichedElement(String additionalSelector) {
//        return getElement(getElementSelector().addSelector(additionalSelector));
//    }
//
//    protected String getEnrichedElementText(String additionalSelector) {
//        return getElementText(getElementSelector().addSelector(additionalSelector));
//    }
//
//    protected void clickEnrichedElement(String additionalSelector) {
//        clickElement(getElementSelector().addSelector(additionalSelector));
//    }
//
//    public void waitUntilAppear(WaitConsts... waitConsts) {
//        waitService.visible(getElement(), waitConsts);
//    }
//
//    public void waitUntilDisappear(WaitConsts... waitConsts) {
//        waitService.invisible(getElement(), waitConsts);
//    }

}