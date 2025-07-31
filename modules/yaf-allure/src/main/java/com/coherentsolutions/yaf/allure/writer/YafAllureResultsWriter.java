package com.coherentsolutions.yaf.allure.writer;

import io.qameta.allure.AllureResultsWriter;

/**
 * The interface Yaf allure results writer.
 */
public interface YafAllureResultsWriter {

    /**
     * Write to results.
     *
     * @param writer the writer
     */
    void writeToResults(AllureResultsWriter writer);
}
