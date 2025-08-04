package com.coherentsolutions.yaf.allure.preprocessor;

import io.qameta.allure.model.TestResult;

/**
 * The interface Test results before write pre-processor.
 */
public interface TestResultsBeforeWritePreProcessor {

    /**
     * Process test results before write.
     *
     * @param testResult the test result
     */
    void processTestResultsBeforeWrite(TestResult testResult);
}
