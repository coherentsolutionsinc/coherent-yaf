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

package com.coherentsolutions.yaf.web.pom.by;


/**
 * The enum Selector type.
 */
public enum SelectorType {

    /**
     * Id selector type.
     */
    ID("id"),
    /**
     * Name selector type.
     */
    NAME("name"),
    /**
     * Class name selector type.
     */
    CLASS_NAME("className"),
    /**
     * Css selector type.
     */
    CSS("css"),
    /**
     * Tag name selector type.
     */
    TAG_NAME("tagName"),
    /**
     * Link text selector type.
     */
    LINK_TEXT("linkText"),
    /**
     * Xpath selector type.
     */
    XPATH("xpath");

    // Do not rename this values. They are used to set up @FindBy proper field;
    private final String type;

    SelectorType(String type) {
        this.type = type;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }
}
