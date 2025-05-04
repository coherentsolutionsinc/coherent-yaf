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
 * Represents the type of folder in the Zephyr Scale system.
 * <p>
 * This enum defines the different types of folders that can exist in the Zephyr Scale system,
 * such as test case folders, test plan folders, and test cycle folders. Each type is associated
 * with a specific string value.
 * </p>
 */
@Getter
@AllArgsConstructor
public enum FolderType {

    /**
     * Testcase folder type.
     */
    TESTCASE("TEST_CASE"),
    /**
     * Test plan folder type.
     */
    TEST_PLAN("TEST_PLAN"),
    /**
     * Test cycle folder type.
     */
    TEST_CYCLE("TEST_CYCLE");

    private final String value;
}
