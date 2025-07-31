package com.coherentsolutions.yaf.allure;

import com.coherentsolutions.yaf.core.test.YafTest;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.TestResult;

public interface KnownIssuesProcessor extends TestLifecycleListener {

    void processTestResultsBeforeTestWrite (YafTest yafTest, TestResult testResult);

//    default void processTestResultsBeforeTestWrite1 (YafTest yafTest, TestResult testResult) {
//        if (yafTest.bugs().length > 0 && testResult.getStatus().equals(Status.FAILED)) {
//            String jiraIds = String.join("; ", yafTest.bugs());
//            StatusDetails details = testResult.getStatusDetails();
//            if (details == null) {
//                details = new StatusDetails();
//            }
//            String originalMessage = details.getMessage() != null ? details.getMessage() : "";
//            String knownIssueMsg = String.format("[Known Issue: %s] %s", jiraIds, originalMessage);
//            details.setMessage(knownIssueMsg);
//            testResult.setStatusDetails(details);
//        }
//    }
}
