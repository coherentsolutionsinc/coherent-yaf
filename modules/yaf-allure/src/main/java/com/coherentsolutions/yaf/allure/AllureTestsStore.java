/*
 * MIT License
 *
 * Copyright (c) 2021 - 2025 Coherent Solutions Inc.
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

package com.coherentsolutions.yaf.allure;

import org.springframework.stereotype.Service;

/**
 * The type Allure tests store.
 */
@Service
public class AllureTestsStore {

    private final ThreadLocal<YafTestContainer> yafTest = new ThreadLocal<>();

    /**
     * Gets test container.
     *
     * @return the test container
     */
    public YafTestContainer getTestContainer() {
        return yafTest.get();
    }

    /**
     * Sets test container.
     *
     * @param testContainer the test container
     */
    public void setTestContainer(YafTestContainer testContainer) {
        yafTest.set(testContainer);
    }

    /**
     * Clear test container.
     */
    public void clearTestContainer() {
        yafTest.remove();
    }
}
