package com.coherentsolutions.yaf.allure.preprocessor;

import com.coherentsolutions.yaf.allure.AllureTestsStore;
import com.coherentsolutions.yaf.allure.YafTestContainer;
import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.test.YafTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * The type Known issues before write pre processor.
 */
@Component
@ConditionalOnProperty(prefix = Consts.FRAMEWORK_NAME + ".allure", name = "use-known-issue-category", havingValue = "true")
public class KnownIssuesBeforeWritePreProcessor implements TestResultsBeforeWritePreProcessor {

    /**
     * The constant KNOWN_ISSUES.
     */
    public static final String KNOWN_ISSUES = "Known Issues";

    /**
     * The Object mapper.
     */
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private AllureTestsStore allureTestsStore;


    /**
     * Gets default json node.
     *
     * @return the default json node
     */
    public JsonNode getDefaultJsonNode() {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("name", KNOWN_ISSUES);
        root.put("messageRegex", ".*\\[" + KNOWN_ISSUES + ": .*\\].*");
        ArrayNode matchedStatuses = objectMapper.createArrayNode();
        matchedStatuses.add("failed");
        root.set("matchedStatuses", matchedStatuses);
        return root;
    }

    @Override
    public void processTestResultsBeforeWrite(TestResult testResult) {
        YafTestContainer container = allureTestsStore.getTestContainer();
        YafTest yafTest = container.getYafTest();
        if (yafTest != null) {
            if (yafTest.bugs().length > 0 && testResult.getStatus().equals(Status.FAILED)) {
                String jiraIds = String.join("; ", yafTest.bugs());
                StatusDetails details = testResult.getStatusDetails();
                if (details == null) {
                    details = new StatusDetails();
                }
                String originalMessage = details.getMessage() != null ? details.getMessage() : "";
                String knownIssueMsg = String.format("[%s: %s] %s", KNOWN_ISSUES, jiraIds, originalMessage);
                details.setMessage(knownIssueMsg);
                testResult.setStatusDetails(details);
            }
        }
    }
}
