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

package com.coherentsolutions.yaf.zephyr.scale.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the status of a test in the Zephyr Scale system.
 * <p>
 * This enum defines the different statuses that a test can have in the Zephyr Scale system,
 * such as pass, fail, and not executed. Each status is associated with a specific string value.
 * </p>
 */
@Getter
@AllArgsConstructor
public enum TestStatus {

    /**
     * Pass test status.
     */
    PASS("Pass"),
    /**
     * Fail test status.
     */
    FAIL("Fail"),
    /**
     * Blocked test status.
     */
    BLOCKED("Blocked"),
    /**
     * The Not execute.
     */
    NOT_EXECUTE("Not Executed");

    private final String value;
}
