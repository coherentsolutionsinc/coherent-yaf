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

package com.coherentsolutions.yaf.testng.utils;

import com.coherentsolutions.yaf.core.utils.StaticContextAccessor;
import com.coherentsolutions.yaf.testng.TestNgProperties;
import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * The type Yaf default retry analyzer.
 */
@Slf4j
public class YafDefaultRetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private int retryMaxCount;

    /**
     * Instantiates a new Yaf default retry analyzer.
     */
    public YafDefaultRetryAnalyzer() {
        TestNgProperties tp = StaticContextAccessor.getBean(TestNgProperties.class);
        retryMaxCount = tp.getRetryMaxCount();
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < retryMaxCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}
