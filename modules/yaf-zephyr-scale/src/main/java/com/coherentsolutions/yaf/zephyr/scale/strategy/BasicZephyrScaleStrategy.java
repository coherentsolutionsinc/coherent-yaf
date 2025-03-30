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

package com.coherentsolutions.yaf.zephyr.scale.strategy;

import com.coherentsolutions.yaf.core.enums.TestState;
import com.coherentsolutions.yaf.core.events.global.ExecutionStartEvent;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.test.model.TestResult;
import com.coherentsolutions.yaf.zephyr.scale.JiraClient;
import com.coherentsolutions.yaf.zephyr.scale.ZephyrScaleClient;
import com.coherentsolutions.yaf.zephyr.scale.ZephyrScaleProperties;
import com.coherentsolutions.yaf.zephyr.scale.constant.ZephyrScaleApiUrl;
import com.coherentsolutions.yaf.zephyr.scale.domain.*;
import com.coherentsolutions.yaf.zephyr.scale.query.EntityRequest;
import com.coherentsolutions.yaf.zephyr.scale.query.TestExecutionReq;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Basic implementation of the {@link ZephyrScaleStrategy} interface for the Zephyr Scale system.
 * <p>
 * This class provides methods to create folders and cycles in the Zephyr Scale system. It uses
 * {@link ZephyrScaleClient} to interact with the Zephyr Scale API and {@link ZephyrScaleProperties}
 * to retrieve configuration properties.
 * </p>
 */
@Slf4j
@Getter
@Component
public class BasicZephyrScaleStrategy implements ZephyrScaleStrategy {

    @Autowired (required = false)
    private ZephyrScaleClient zephyrScaleClient;
    @Autowired (required = false)
    private JiraClient jiraClient;
    @Autowired
    private ZephyrScaleProperties zephyrScaleProperties;

    private Folder folder;
    private Cycle cycle;
    private Set<String> testKeys = new HashSet<>();

    /**
     * Creates the necessary folders in the Zephyr Scale system.
     * <p>
     * This method checks if the root folder exists and creates it if it does not.
     * </p>
     */
    @Override
    public void createFolders(ExecutionStartEvent executionStartEvent) {
        List<String> foldersList = Arrays.asList(zephyrScaleProperties.getFolders().split("/"));
        AtomicReference<Integer> parentId = new AtomicReference<>();
        foldersList.forEach(x -> {
            folder = zephyrScaleClient.createFolderIfNotExist(x, parentId.get(), FolderType.TEST_CYCLE);
            parentId.set(folder.getId());
        });
    }

    /**
     * Creates a test cycle in the Zephyr Scale system.
     * <p>
     * This method creates a new test cycle based on the provided {@link ExecutionStartEvent}.
     * The cycle name is generated using the suite name and the current date and time.
     * </p>
     *
     * @param executionStartEvent the event that triggers the creation of the test cycle
     */
    @Override
    public void createCycle(ExecutionStartEvent executionStartEvent) {
        CycleCreateDTO cycleCreateDTO = new CycleCreateDTO();
        cycleCreateDTO.setProjectKey(zephyrScaleProperties.getProjectKey());
        cycleCreateDTO.setName(zephyrScaleProperties.getCycleName());
        cycleCreateDTO.setFolderId(this.folder.getId());
        this.cycle = zephyrScaleClient.createCycleIfNotExist(cycleCreateDTO);
    }

    /**
     * Captures the test keys for the current test cycle.
     * <p>
     * This method retrieves the test executions associated with the current test cycle
     * and extracts the test case keys from them. The keys are stored in the {@code testKeys} set.
     * </p>
     */
    @Override
    public void captureTestKeys() {
        if (testKeys.isEmpty()) {
            String testCycleKey = zephyrScaleProperties.getCycleKey();
            try {
                List<TestExecution> testExecutions = zephyrScaleClient.getExecutionsRequest().query(new TestExecutionReq().setTestCycle(testCycleKey));
                Pattern pattern = Pattern.compile("testcases/([^/]+)");
                testKeys = testExecutions.stream()
                        .map(x -> pattern.matcher(x.getTestCase().getSelf()))
                        .filter(Matcher::find)
                        .map(matcher -> matcher.group(1))
                        .collect(Collectors.toSet());
            } catch (Exception e) {
                log.error("Cannot capture test ids for cycle with [{}] key", testCycleKey);
            }
        }
    }

    /**
     * Retrieves the test cycle key.
     * <p>
     * If the cycle key is not provided in the properties, this method returns the key of the current cycle.
     * Otherwise, it returns the cycle key from the properties.
     * </p>
     *
     * @return the test cycle key
     */
    public String getTestCycleKey() {
        return StringUtils.isBlank(zephyrScaleProperties.getCycleKey()) ? this.cycle.getKey() : zephyrScaleProperties.getCycleKey();
    }

    /**
     * Prepares a test execution object with the necessary details.
     * <p>
     * This method sets the execution time and status of the test execution object based on the
     * provided {@link TestFinishEvent}. If the test result is not successful, it also sets the
     * error comment.
     * </p>
     *
     * @param <T>              the type of the test execution object
     * @param obj              the test execution object to prepare
     * @param testFinishEvent  the event that contains the test result
     * @return the prepared test execution object
     */
    private <T extends TestExecutionUpdateDTO> T prepareTestExecution(T obj, TestFinishEvent testFinishEvent) {
        TestResult testResult = testFinishEvent.getTestResult();
        obj.setExecutionTime(testResult.getExecutionTime());

        TestStatus status = switch (testResult.getState()) {
            case SUCCESS -> TestStatus.PASS;
            case FAIL -> TestStatus.FAIL;
            case SKIP -> TestStatus.BLOCKED;
            default -> TestStatus.NOT_EXECUTE;
        };
        obj.setStatusName(status);

        if (!testResult.isSuccess()) {
            obj.setComment(ExceptionUtils.getStackTrace(testResult.getError()));
        }

        return obj;
    }

    /**
     * Creates an issue link in the Zephyr Scale system.
     * <p>
     * This method creates a link between a test execution and an issue in the Zephyr Scale system.
     * It uses the provided test execution and test finish event to establish the link.
     * </p>
     *
     * @param execution       the test execution to link
     * @param testFinishEvent the event that contains the test result and issue information
     */
    private void createIssueLink(TestExecution execution, TestFinishEvent testFinishEvent) {
        if (Objects.nonNull(jiraClient) && Objects.nonNull(execution)) {
            Arrays.stream(testFinishEvent.getTestInfo().getYafTest().bugs()).forEach(x -> {
                Integer jiraIssueId = jiraClient.getJiraIssueId(x);
                if (Objects.nonNull(jiraIssueId)) {
                    zephyrScaleClient.createIssueLink(execution.getId(), jiraIssueId);
                }
            });
        }
    }

    /**
     * Checks for existing bugs linked to the test case.
     * <p>
     * This method logs a warning if the test has passed but has bugs linked to it.
     * </p>
     *
     * @param testFinishEvent the event that contains the test result
     * @param testKey         the test case key
     */
    private void checkExistingBugs(TestFinishEvent testFinishEvent, String testKey) {
        if (testFinishEvent.getTestResult().getState().equals(TestState.SUCCESS) && testFinishEvent.getTestInfo().getYafTest().bugs().length > 0) {
            log.warn("[{}] Test has passed, but has bugs linked to it", testKey);
        }
    }

    /**
     * Creates a test execution in the Zephyr Scale system.
     * <p>
     * This method creates a test execution record based on the provided test case key
     * and test finish event. It sets the test status, execution time, and any error comments.
     * </p>
     *
     * @param testKey         the test case key
     * @param testFinishEvent the event that contains the test result
     */
    @Override
    public void createTestExecution(String testKey, TestFinishEvent testFinishEvent) {
        TestExecutionCreateDTO testExecution = prepareTestExecution(new TestExecutionCreateDTO(), testFinishEvent);
        testExecution.setTestCaseKey(testKey);
        testExecution.setTestCycleKey(getTestCycleKey());

        checkExistingBugs(testFinishEvent, testKey);

        TestExecution execution = null;
        try {
            execution = zephyrScaleClient.getExecutionsRequest().create(testExecution);
            log.info("Test execution with [{}] result successfully added for [{}] test in [{}] cycle", testFinishEvent.getTestResult().getState(), testKey, testExecution.getTestCycleKey());
        } catch (Exception e) {
            log.error("Test execution with [{}] result is NOT added for [{}] test in [{}] cycle", testFinishEvent.getTestResult().getState(), testKey, testExecution.getTestCycleKey());
        }
        createIssueLink(execution, testFinishEvent);
    }

    /**
     * Updates the test execution in the Zephyr Scale system.
     * <p>
     * This method updates an existing test execution record based on the provided test case key
     * and test finish event. It sets the test status, execution time, and any error comments.
     * If the test execution is not found, it logs a warning.
     * </p>
     *
     * @param testKey         the test case key
     * @param testFinishEvent the event that contains the test result
     */
    @Override
    public void updateTestExecution(String testKey, TestFinishEvent testFinishEvent) {
        if (testKeys.contains(testKey)) {
            String cycleKey = getTestCycleKey();
            checkExistingBugs(testFinishEvent, testKey);

            TestExecutionUpdateDTO testExecutionUpdateDTO = prepareTestExecution(new TestExecutionUpdateDTO(), testFinishEvent);
            List<TestExecution> testExecutions = zephyrScaleClient.getExecutionsRequest().query(new TestExecutionReq().setTestCycle(cycleKey).setTestCase(testKey));
            if (!testExecutions.isEmpty()) {
                String testExecutionName = testExecutions.get(testExecutions.size() - 1).getName();
                String url = String.format(ZephyrScaleApiUrl.UPDATE_EXECUTIONS, testExecutionName);
                EntityRequest<TestExecutionUpdateDTO, TestExecutionReq> testExecutionRequest = new EntityRequest<>(zephyrScaleClient.getRequestSpecification(), TestExecutionUpdateDTO.class, url, zephyrScaleProperties.getProjectKey());
                try {
                    String response = testExecutionRequest.update(testExecutionUpdateDTO);
                    if (response.isBlank()) {
                        log.info("[{}] Test execution with [{}] result successfully updated for [{}] test in [{}] cycle", testExecutionName, testFinishEvent.getTestResult().getState(), testKey, cycleKey);
                    } else {
                        log.error("Test execution with [{}] result is NOT updated for [{}] test in [{}] cycle due to [{}]", testFinishEvent.getTestResult().getState(), testKey, cycleKey, response);
                    }
                } catch (Exception e) {
                    log.warn("Cannot update test execution with [{}] result for [{}] test in [{}] cycle, exception {}", testFinishEvent.getTestResult().getState(), testKey, cycleKey, e.getMessage());
                }
                createIssueLink(testExecutions.get(testExecutions.size() - 1), testFinishEvent);
            } else {
                log.warn("Test execution for [{}] test in [{}] cycle is not found", testKey, cycleKey);
            }
        } else {
            log.warn("[{}] Test case is not found in [{}] cycle", testKey, getTestCycleKey());
        }
    }
}
