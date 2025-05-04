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

package com.coherentsolutions.yaf.zephyr.scale.strategy;

import com.coherentsolutions.yaf.core.events.global.ExecutionStartEvent;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;

/**
 * Strategy interface for Zephyr Scale operations.
 * <p>
 * This interface defines the methods required for creating folders and test cycles
 * in the Zephyr Scale system. Implementations of this interface should provide the
 * logic for interacting with the Zephyr Scale API to manage these entities.
 * </p>
 */
public interface ZephyrScaleStrategy {

    /**
     * Create folders.
     *
     * @param executionStartEvent the execution start event
     */
    void createFolders(ExecutionStartEvent executionStartEvent);

    /**
     * Create cycle.
     *
     * @param executionStartEvent the execution start event
     */
    void createCycle(ExecutionStartEvent executionStartEvent);

    /**
     * Capture test keys.
     */
    void captureTestKeys();

    /**
     * Create test execution.
     *
     * @param testKey         the test key
     * @param testFinishEvent the test finish event
     */
    void createTestExecution(String testKey, TestFinishEvent testFinishEvent);

    /**
     * Update test execution.
     *
     * @param testKey         the test key
     * @param testFinishEvent the test finish event
     */
    void updateTestExecution(String testKey, TestFinishEvent testFinishEvent);
}
