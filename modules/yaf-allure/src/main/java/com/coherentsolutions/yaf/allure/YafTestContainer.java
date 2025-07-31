package com.coherentsolutions.yaf.allure;

import com.coherentsolutions.yaf.core.test.YafTest;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import com.coherentsolutions.yaf.core.test.model.TestResult;
import lombok.Data;

/**
 * The type Yaf test container.
 */
@Data
public class YafTestContainer {

    /**
     * The Yaf test.
     */
    YafTest yafTest;
    /**
     * The Test info.
     */
    TestInfo testInfo;
    /**
     * The Test result.
     */
    TestResult testResult;

    /**
     * Instantiates a new Yaf test container.
     *
     * @param testInfo the test info
     */
    public YafTestContainer(TestInfo testInfo) {
        this.testInfo = testInfo;
        this.yafTest = testInfo.getYafTest();
    }
}
