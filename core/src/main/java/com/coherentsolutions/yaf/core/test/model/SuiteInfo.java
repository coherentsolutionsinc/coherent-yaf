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

package com.coherentsolutions.yaf.core.test.model;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The type Suite info.
 */
@Data
@Accessors(chain = true)
public class SuiteInfo {

    /**
     * The Suite id.
     */
    @Getter(AccessLevel.NONE)
    String suiteId;
    /**
     * The Suite name.
     */
    String suiteName;
    /**
     * The Suite params.
     */
    Map<String, String> suiteParams;
    /**
     * The Test methods.
     */
    Set<Method> testMethods;
    // TODO add info about parallelization

    /**
     * Gets suite id.
     *
     * @return the suite id
     */
    public String getSuiteId() {
        if (suiteId == null) {
            suiteId = suiteName + "_" + Objects.hash(suiteName, suiteParams);
        }
        return suiteId;
    }
}
