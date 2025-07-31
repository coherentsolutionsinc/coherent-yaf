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

import com.coherentsolutions.yaf.core.enums.Severity;
import com.coherentsolutions.yaf.core.test.YafTest;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Link;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.util.ResultsUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Test patcher.
 */
@Component
public class TestPatcher implements TestLifecycleListener {

    /**
     * The Test store.
     */
    @Getter
    Map<String, YafTest> testStore;

    @Autowired(required = false)
    List<KnownIssuesProcessor>  knownIssuesProcessors;

    /**
     * Sets test store.
     *
     * @param testStore the test store
     */
    public void setTestStore(Map<String, YafTest> testStore) {
        this.testStore = testStore;
    }

    /**
     * Patches allure test result.
     *
     * @param result testResult
     */
    @Override
    public void beforeTestWrite(final TestResult result) {
        YafTest yafTest = testStore.remove(result.getFullName());
        if (yafTest != null) {
            result.setName(getTestName(yafTest, result));
            result.setDescription(getDescription(yafTest, result));
            // TODO may add other options
            result.setLabels(getLabels(yafTest, result));
            result.setLinks(getLinks(yafTest, result));
            if (knownIssuesProcessors != null) {
                knownIssuesProcessors.forEach(processor -> {processor.processTestResultsBeforeTestWrite(yafTest, result);});
            }
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