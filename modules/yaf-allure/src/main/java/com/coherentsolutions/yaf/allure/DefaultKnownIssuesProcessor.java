package com.coherentsolutions.yaf.allure;

import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.test.YafTest;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.TestResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = Consts.FRAMEWORK_NAME + ".allure", name = "use-known-issue-category", havingValue = "true")
public class DefaultKnownIssuesProcessor implements KnownIssuesProcessor {

    @Override
    public void processTestResultsBeforeTestWrite(YafTest yafTest, TestResult testResult) {
        if (yafTest.bugs().length > 0 && testResult.getStatus().equals(Status.FAILED)) {
            String jiraIds = String.join("; ", yafTest.bugs());
            StatusDetails details = testResult.getStatusDetails();
            if (details == null) {
                details = new StatusDetails();
            }
            String originalMessage = details.getMessage() != null ? details.getMessage() : "";
            String knownIssueMsg = String.format("[Known Issue: %s] %s", jiraIds, originalMessage);
            details.setMessage(knownIssueMsg);
            testResult.setStatusDetails(details);
        }
    }


//    void processTestResultsBeforeTestWrite (YafTest yafTest, TestResult testResult) {
//        if (yafTest.bugs().length > 0 && testResult.getStatus().equals(Status.FAILED)) {
//        String jiraIds = String.join("; ", yafTest.bugs());
//        StatusDetails details = testResult.getStatusDetails();
//        if (details == null) {
//            details = new StatusDetails();
//        }
//        String originalMessage = details.getMessage() != null ? details.getMessage() : "";
//        String knownIssueMsg = String.format("[Known Issue: %s] %s", jiraIds, originalMessage);
//        details.setMessage(knownIssueMsg);
//        testResult.setStatusDetails(details);
//    }
    }
