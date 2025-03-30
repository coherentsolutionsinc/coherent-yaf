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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for creating a test execution in the Zephyr Scale system.
 * <p>
 * This class extends {@link TestExecution} and includes additional properties specific to the creation
 * of a test execution, such as project key, test case key, test cycle key, status name, test script results,
 * environment name, and execution time.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
public class TestExecutionCreateDTO extends TestExecutionUpdateDTO {

    private String projectKey;
    private String testCaseKey;
    private String testCycleKey;
    private TestStatus statusName;
    private List<TestScriptResult> testScriptResults;
    private String environmentName;
    private Long executionTime;

    /**
     * Represents a test script result in the Zephyr Scale system.
     * <p>
     * This class includes properties specific to test script results, such as status name,
     * actual end date, and actual result.
     * </p>
     */
    @Data
    @Accessors(chain = true)
    public static class TestScriptResult {

        private TestStatus statusName;
        private Date actualEndDate;
        private String actualResult;
    }

}
