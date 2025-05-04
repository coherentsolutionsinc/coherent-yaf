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

package com.coherentsolutions.yaf.web.utils.component;

import com.coherentsolutions.yaf.web.pom.by.YafSetParam;

import java.lang.annotation.Annotation;

/**
 * Builder class for creating instances of {@link YafSetParam}.
 * <p>
 * This builder class provides methods to set the key and value for a YafSetParam
 * and then build the YafSetParam instance.
 * </p>
 */
class YafSetParamBuilder {

    private String key;
    private String value;

    private YafSetParamBuilder() {
    }

    /**
     * Creates a new instance of {@code YafSetParamBuilder}.
     *
     * @return a new instance of {@code YafSetParamBuilder}
     */
    public static YafSetParamBuilder builder() {
        return new YafSetParamBuilder();
    }

    /**
     * Sets the key for the YafSetParam.
     *
     * @param key the key to set
     * @return the current instance of {@code YafSetParamBuilder}
     */
    public YafSetParamBuilder key(String key) {
        this.key = key;
        return this;
    }

    /**
     * Sets the value for the YafSetParam.
     *
     * @param value the value to set
     * @return the current instance of {@code YafSetParamBuilder}
     */
    public YafSetParamBuilder value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Builds and returns the configured {@code YafSetParam} instance.
     *
     * @return the configured instance of {@code YafSetParam}
     */
    public YafSetParam build() {
        return new YafSetParam() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return YafSetParam.class;
            }

            @Override
            public String key() {
                return key;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}