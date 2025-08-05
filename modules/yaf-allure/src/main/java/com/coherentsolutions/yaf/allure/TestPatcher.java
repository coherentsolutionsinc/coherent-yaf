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

package com.coherentsolutions.yaf.allure;

import com.coherentsolutions.yaf.allure.preprocessor.TestResultsBeforeWritePreProcessor;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * The type Test patcher.
 */
@Component
@Slf4j
public class TestPatcher implements TestLifecycleListener {


    /**
     * The Test results before write pre-processors.
     */
    @Autowired(required = false)
    protected List<TestResultsBeforeWritePreProcessor> testResultsBeforeWritePreProcessors;
    /**
     * The Allure tests store.
     */
    @Autowired
    AllureTestsStore allureTestsStore;

    /**
     * Patches allure test result.
     *
     * @param result testResult
     */
    @Override
    public void beforeTestWrite(final TestResult result) {
        YafTestContainer yafTestContainer = allureTestsStore.getTestContainer();
        if (yafTestContainer != null) {
            if (testResultsBeforeWritePreProcessors != null) {
                testResultsBeforeWritePreProcessors.forEach(processor -> {
                    try {
                        processor.processTestResultsBeforeWrite(result);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
            }
        }
    }
}