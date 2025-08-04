package com.coherentsolutions.yaf.allure.preprocessor;

import com.coherentsolutions.yaf.allure.AllureTestsStore;
import com.coherentsolutions.yaf.allure.YafTestContainer;
import com.coherentsolutions.yaf.core.enums.Severity;
import com.coherentsolutions.yaf.core.test.YafTest;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Link;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.util.ResultsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Yaf test annotation before write pre processor.
 */
@Service
@Slf4j
@Order(0)
public class YafTestAnnotationBeforeWritePreProcessor implements TestResultsBeforeWritePreProcessor {

    /**
     * The Allure tests store.
     */
    @Autowired
    AllureTestsStore allureTestsStore;

    @Override
    public void processTestResultsBeforeWrite(TestResult testResult) {
        YafTestContainer container = allureTestsStore.getTestContainer();
        YafTest yafTest = container.getYafTest();
        if (yafTest != null) {
            testResult.setName(getTestName(yafTest, testResult));
            testResult.setDescription(getDescription(yafTest, testResult));
            testResult.setLabels(getLabels(yafTest, testResult));
            testResult.setLinks(getLinks(yafTest, testResult));
        }
    }

    /**
     * Get test name string.
     *
     * @param yafTest the yaf test
     * @param result  the result
     * @return the string
     */
    protected String getTestName(YafTest yafTest, TestResult result) {
        return yafTest.name();
    }

    /**
     * Get description string.
     *
     * @param yafTest the yaf test
     * @param result  the result
     * @return the string
     */
    protected String getDescription(YafTest yafTest, TestResult result) {
        return yafTest.description();
    }

    /**
     * Get labels list.
     *
     * @param yafTest the yaf test
     * @param result  the result
     * @return the list
     */
    protected List<Label> getLabels(YafTest yafTest, TestResult result) {
        List<Label> labels = result.getLabels();
        if (labels == null) {
            labels = new ArrayList<>();
        }
        if (!yafTest.severity().equals(Severity.UNKNOWN)) {
            Label severity = ResultsUtils.createSeverityLabel(yafTest.severity().name());
            labels.add(severity);
        }
        labels.add(ResultsUtils.createThreadLabel());
        return labels;
    }

    /**
     * Get links list.
     *
     * @param yafTest the yaf test
     * @param result  the result
     * @return the list
     */
    protected List<Link> getLinks(YafTest yafTest, TestResult result) {
        List<Link> links = result.getLinks();
        if (links == null) {
            links = new ArrayList<>();
        }
        if (yafTest.tmsIds().length > 0) {
            links.addAll(Arrays.stream(yafTest.tmsIds()).map(l -> ResultsUtils.createTmsLink(l))
                    .collect(Collectors.toList()));
        }
        if (yafTest.bugs().length > 0) {
            links.addAll(Arrays.stream(yafTest.bugs()).map(b -> ResultsUtils.createIssueLink(b))
                    .collect(Collectors.toList()));
        }
        return links;
    }
}
