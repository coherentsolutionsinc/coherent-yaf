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
import lombok.Getter;

/**
 * The enum Finish event data type.
 */
public enum FinishEventDataType implements YafEnum {
    /**
     * Driver log finish event data type.
     */
    DRIVER_LOG("driverLog", "text/plain", "log"),
    /**
     * Screenshot finish event data type.
     */
    SCREENSHOT("screenshot", "image/png", "png"),
    /**
     * Api log finish event data type.
     */
    API_LOG("apiLog", "application/json", "json"),
    /**
     * Param finish event data type.
     */
    PARAM("param", "text/plain", "txt"),
    /**
     * Page source finish event data type.
     */
    PAGE_SOURCE("pageSource", "text/html", "html");

    @JsonValue
    private final String name;
    @Getter
    private final String dataType;
    @Getter
    private final String fileExt;

    FinishEventDataType(String name, String dataType, String fileExt) {
        this.name = name;
        this.dataType = dataType;
        this.fileExt = fileExt;
    }

    /**
     * Value of name finish event data type.
     *
     * @param name the name
     * @return the finish event data type
     */
    public static FinishEventDataType valueOfName(String name) {
        for (FinishEventDataType e : values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }

}
