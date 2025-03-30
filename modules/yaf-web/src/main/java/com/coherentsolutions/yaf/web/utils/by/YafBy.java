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

package com.coherentsolutions.yaf.web.utils.by;


import com.coherentsolutions.yaf.web.pom.by.SelectorType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * The type Yaf by.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class YafBy extends By {

    /**
     * The Type.
     */
    SelectorType type;
    /**
     * The Selector.
     */
    String selector;
    /**
     * The By.
     */
    By by;

    /**
     * Instantiates a new Yaf by.
     *
     * @param by the by
     */
    public YafBy(By by) {
        this.by = by;
    }

    /**
     * Instantiates a new Yaf by.
     *
     * @param type     the type
     * @param selector the selector
     */
    public YafBy(SelectorType type, String selector) {
        this.type = type;
        this.selector = selector;
    }

    /**
     * Css yaf by.
     *
     * @param selector the selector
     * @return the yaf by
     */
    public static YafBy css(String selector) {
        return new YafBy(SelectorType.CSS, selector);
    }

    /**
     * Xpath yaf by.
     *
     * @param selector the selector
     * @return the yaf by
     */
    public static YafBy xpath(String selector) {
        return new YafBy(SelectorType.XPATH, selector);
    }

    /**
     * By yaf by.
     *
     * @param by the by
     * @return the yaf by
     */
    public static YafBy by(By by) {
        return new YafBy(by);
    }

    /**
     * Add selector yaf by.
     *
     * @param extraSelector the extra selector
     * @return the yaf by
     */
    public YafBy addSelector(String extraSelector) {
        return new YafBy(type, selector + (type.equals(SelectorType.CSS) ? " " : "") + extraSelector);
    }

    /**
     * Gets by.
     *
     * @return the by
     */
    public By getBy() {
        if (by != null) {
            return by;
        }
        return switch (type) {
            case ID -> By.id(selector);
            case NAME -> By.name(selector);
            case CLASS_NAME -> By.className(selector);
            case CSS -> By.cssSelector(selector);
            case TAG_NAME -> By.tagName(selector);
            case LINK_TEXT -> By.linkText(selector);
            case XPATH -> By.xpath(selector);
        };
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return context.findElements(getBy());
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return context.findElement(getBy());
    }

}
