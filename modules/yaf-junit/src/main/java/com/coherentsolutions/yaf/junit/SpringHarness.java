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

package com.coherentsolutions.yaf.junit;

import org.springframework.test.context.TestContextManager;

import java.lang.reflect.Method;

class SpringHarness {
    private final TestContextManager manager;
    private final Class<?> testClass;

    SpringHarness(Class<?> testClass) {
        this.testClass = testClass;
        this.manager = new TestContextManager(testClass); // reads @SpringBootTest, etc.
    }

    void beforeAll() throws Exception {
        manager.beforeTestClass();
    }

    void prepare(Object testInstance) throws Exception {
        manager.prepareTestInstance(testInstance); // @Autowired fields, etc.
    }

    void beforeEach(Object testInstance, Method method) throws Exception {
        manager.beforeTestMethod(testInstance, method);
    }

    void afterEach(Object testInstance, Method method, Throwable thrown) throws Exception {
        manager.afterTestMethod(testInstance, method,
                (thrown == null) ? null : thrown);
    }

    void afterAll() throws Exception {
        manager.afterTestClass();
    }
}