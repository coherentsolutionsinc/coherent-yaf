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
 * The enum Driver scope.
 */
public enum DriverScope implements YafEnum {
    /**
     * Execution driver scope.
     */
    EXECUTION("execution"),
    /**
     * Suite driver scope.
     */
    SUITE("suite"),
    /**
     * Class driver scope.
     */
    CLASS("class"),
    /**
     * Method driver scope.
     */
    METHOD("method");

    @JsonValue
    private final String name;

    DriverScope(String name) {
        this.name = name;
    }

    /**
     * Value of name driver scope.
     *
     * @param name the name
     * @return the driver scope
     */
    public static DriverScope valueOfName(String name) {
        for (DriverScope e : values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
