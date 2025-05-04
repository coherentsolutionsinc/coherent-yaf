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

package com.coherentsolutions.yaf.report.domain;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Suite report.
 */
@Data
@Accessors(chain = true)
public class SuiteReport {

    /**
     * The Suite id.
     */
    String suiteId;
    /**
     * The Name.
     */
    String name;
    /**
     * The Suite params.
     */
    Map<String, String> suiteParams;
    /**
     * The Test reports.
     */
    List<TestReport> testReports;

    /**
     * Instantiates a new Suite report.
     */
    public SuiteReport() {
        suiteParams = new HashMap<>();
        testReports = new ArrayList<>();
    }

    /**
     * Gets string stats.
     *
     * @return the string stats
     */
    public String getStringStats() {
        int success = 0;
        int fail = 0;
        int skip = 0;
        for (TestReport r : testReports) {
            switch (r.getTestResult().getState()) {
                case FAIL: {
                    fail += 1;
                    break;
                }
                case SKIP: {
                    skip += 1;
                    break;
                }
                case SUCCESS: {
                    success += 1;
                    break;
                }
            }
        }
        return "Success: " + success + " Fail: " + fail + " Skip: " + skip;
    }
}
