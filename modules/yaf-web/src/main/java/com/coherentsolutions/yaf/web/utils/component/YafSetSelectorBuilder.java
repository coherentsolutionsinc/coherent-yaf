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

import com.coherentsolutions.yaf.web.pom.by.SelectorType;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;

import java.lang.annotation.Annotation;

/**
 * Builder class for creating instances of {@link YafSetSelector}.
 * <p>
 * This builder class provides methods to set the field, value, and type for a YafSetSelector
 * and then build the YafSetSelector instance.
 * </p>
 */
class YafSetSelectorBuilder {

    private String field = "root";
    private String value = "";
    private SelectorType type = SelectorType.CSS;

    private YafSetSelectorBuilder() {
    }

    /**
     * Creates a new instance of {@code YafSetSelectorBuilder}.
     *
     * @return a new instance of {@code YafSetSelectorBuilder}
     */
    public static YafSetSelectorBuilder builder() {
        return new YafSetSelectorBuilder();
    }

    /**
     * Sets the field for the YafSetSelector.
     *
     * @param field the field to set
     * @return the current instance of {@code YafSetSelectorBuilder}
     */
    public YafSetSelectorBuilder field(String field) {
        this.field = field.isEmpty() ? "root" : field;
        return this;
    }

    /**
     * Sets the value for the YafSetSelector.
     *
     * @param value the value to set
     * @return the current instance of {@code YafSetSelectorBuilder}
     */
    public YafSetSelectorBuilder value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Sets the type for the YafSetSelector.
     *
     * @param type the type to set
     * @return the current instance of {@code YafSetSelectorBuilder}
     */
    public YafSetSelectorBuilder type(SelectorType type) {
        this.type = (type == null) ? SelectorType.CSS : type;
        return this;
    }

    /**
     * Builds and returns the configured {@code YafSetSelector} instance.
     *
     * @return the configured instance of {@code YafSetSelector}
     */
    public YafSetSelector build() {
        return new YafSetSelector() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return YafSetSelector.class;
            }

            @Override
            public String field() {
                return field;
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public SelectorType type() {
                return type;
            }
        };
    }

}