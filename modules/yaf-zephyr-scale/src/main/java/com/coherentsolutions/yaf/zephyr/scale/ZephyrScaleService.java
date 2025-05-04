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

package com.coherentsolutions.yaf.zephyr.scale;

import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.events.global.ExecutionStartEvent;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.test.YafTest;
import com.coherentsolutions.yaf.zephyr.scale.strategy.ZephyrScaleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Service for managing Zephyr Scale operations.
 * <p>
 * This service handles the creation of test cycles and logging of test executions
 * in the Zephyr Scale system. It listens to test start and finish events and interacts
 * with the Zephyr Scale API through the {@link ZephyrScaleClient}.
 * </p>
 */
@Service
@Slf4j
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".zephyr.scale.enabled", havingValue = "true")
public class ZephyrScaleService {

    @Autowired
    private ZephyrScaleProperties zephyrScaleProperties;

    @Autowired
    private ZephyrScaleStrategy zephyrScaleStrategy;

    @Autowired
    private ZephyrScaleClient zephyrScaleClient;

    /**
     * Creates a Zephyr test cycle when a test starts.
     * <p>
     * This method is triggered by the {@link ExecutionStartEvent} and creates the necessary
     * folders and test cycle in the Zephyr Scale system if the cycle key is not provided.
     * </p>
     *
     * @param executionStartEvent the event that triggers the creation of the test cycle
     */
    @EventListener
    public void createZephyrCycle(ExecutionStartEvent executionStartEvent) {
        if (StringUtils.isBlank(zephyrScaleProperties.getCycleKey())) {
            zephyrScaleStrategy.createFolders(executionStartEvent);
            zephyrScaleStrategy.createCycle(executionStartEvent);
        }
        if (zephyrScaleProperties.getExecutionStrategy().equals(ZephyrScaleExecutionStrategy.UPDATE)) {
            zephyrScaleStrategy.captureTestKeys();
        }
    }

    /**
     * Logs the test execution when a test finishes.
     * <p>
     * This method is triggered by the {@link TestFinishEvent} and logs the test execution
     * in the Zephyr Scale system for each test case key associated with the test.
     * </p>
     *
     * @param testFinishEvent the event that triggers the logging of the test execution
     */
    @EventListener
    public void logTestExecution(TestFinishEvent testFinishEvent) {
        YafTest yafTest = testFinishEvent.getTestInfo().getYafTest();
        if (yafTest != null && yafTest.tmsIds().length > 0) {
            Arrays.stream(yafTest.tmsIds()).forEach(testKey -> {
                if (zephyrScaleProperties.getExecutionStrategy().equals(ZephyrScaleExecutionStrategy.UPDATE)) {
                    zephyrScaleStrategy.updateTestExecution(testKey, testFinishEvent);
                } else {
                    zephyrScaleStrategy.createTestExecution(testKey, testFinishEvent);
                }
            });
        }
    }


}
