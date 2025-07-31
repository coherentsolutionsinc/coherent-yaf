package com.coherentsolutions.yaf.allure;

import com.coherentsolutions.yaf.core.test.YafTest;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.TestResult;

public interface KnownIssuesProcessor extends TestLifecycleListener {

    void processTestResultsBeforeTestWrite (YafTest yafTest, TestResult testResult);
}
