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

package com.coherentsolutions.yaf.core.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Browser.
 */
public enum Browser implements YafEnum {
    /**
     * Chrome browser.
     */
    CHROME("chrome"),
    /**
     * Ff browser.
     */
    FF("ff"),
    /**
     * Safari browser.
     */
    SAFARI("safari"),
    /**
     * Edge browser.
     */
    EDGE("edge"),
    /**
     * Html unit browser.
     */
    HTML_UNIT("htmlunit"),
    /**
     * Other browser.
     */
    OTHER("other");

    @JsonValue
    private final String name;

    Browser(String name) {
        this.name = name;
    }

    /**
     * Value of name browser.
     *
     * @param name the name
     * @return the browser
     */
    public static Browser valueOfName(String name) {
        for (Browser e : values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
