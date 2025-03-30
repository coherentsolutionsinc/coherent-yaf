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

package com.coherentsolutions.yaf.core.listener;


import com.coherentsolutions.yaf.core.context.execution.ExecutionContext;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.events.global.ExecutionStartEvent;
import com.coherentsolutions.yaf.core.events.global.SuiteStartEvent;
import com.coherentsolutions.yaf.core.events.test.ClassFinishEvent;
import com.coherentsolutions.yaf.core.events.test.RawTestFinishEvent;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.events.test.TestStartEvent;
import com.coherentsolutions.yaf.core.exec.EnvironmentService;
import com.coherentsolutions.yaf.core.exec.ExecutionService;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import com.coherentsolutions.yaf.core.log.TestLogData;
import com.coherentsolutions.yaf.core.log.properties.TestFinishProperties;
import com.coherentsolutions.yaf.core.processor.TestFinishEventProcessor;
import com.coherentsolutions.yaf.core.utils.PropertiesUtils;
import com.coherentsolutions.yaf.core.utils.ServiceProviderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The type Global yaf events listener.
 */
@Component
@Slf4j
public class GlobalYafEventsListener extends YafListener {

    /**
     * The Properties utils.
     */
    @Autowired
    PropertiesUtils propertiesUtils;

    /**
     * The Execution context.
     */
    @Autowired
    ExecutionContext executionContext;
    /**
     * The Finish event processor list.
     */
    @Autowired(required = false)
    List<TestFinishEventProcessor> finishEventProcessorList;
    /**
     * The Properties.
     */
    @Autowired
    TestFinishProperties properties;

    /**
     * The Environment service.
     */
    @Autowired
    EnvironmentService environmentService;

    /**
     * The Inited.
     */
    boolean inited;

    // /**
    // * This is hack method, cause spring does not send start event on early phases
    // */
    // @PostConstruct
    // protected void sendStartEvent() { // TODO double check?
    // if (!inited) {
    // Map<String, String> mapProperties = (Map) executionContext.getProperties();
    // ExecutionStartEvent executionStartEvent = new ExecutionStartEvent();
    // executionStartEvent.setStartTime(LocalDateTime.now())
    // .setEnvConfigName(ExecutionService.getInstance().getConfiguration().getName())
    // .setTestEnv(executionContext.getTestEnv()).setEnvProps(mapProperties);
    //
    // eventsService.sendEvent(executionStartEvent);
    // inited = true;
    // }
    // }

    /**
     * Before suite.
     *
     * @param event the event
     */
    @EventListener
    @Order(1)
    public void beforeSuite(SuiteStartEvent event) {
        if (!inited) {
            synchronized (GlobalYafEventsListener.class) {
                Map<String, String> mapProperties = (Map) executionContext.getProperties();
                ExecutionStartEvent executionStartEvent = new ExecutionStartEvent();
                ExecutionService executionService = ServiceProviderUtils.getExecutionService();
                executionStartEvent.setStartTime(LocalDateTime.now())
                        .setEnvConfigName(executionService.getConfiguration().getName())
                        .setTestEnv(executionContext.getTestEnv()).setEnvProps(mapProperties);

                eventsService.sendEvent(executionStartEvent);
                inited = true;
            }
        }
    }


    /**
     * Before test.
     *
     * @param event the event
     */
    @EventListener
    @Order(1)
    public void beforeTest(TestStartEvent event) {

        TestExecutionContext testExecutionContext = getTestExecutionContext();
        Environment env = environmentService.getProperTestEnvironment(event.getTestInfo().getEnvSetup(), testExecutionContext);
        testExecutionContext.buildFromTestStartEvent(event, env);

        event.getTestClassInstance().initTestFields();
    }

    /**
     * After test.
     *
     * @param event the event
     */
    @EventListener(RawTestFinishEvent.class)
    @Order(1)
    public void afterTest(RawTestFinishEvent event) {
        // gets raw test finish event, we need to append additional data
        log.info("After test {}", event.getTestInfo().getFullTestName());
        TestFinishEvent testFinishEvent = new TestFinishEvent();
        testFinishEvent.setTestInfo(event.getTestInfo());
        testFinishEvent.setTestResult(event.getTestResult());

        TestExecutionContext testExecutionContext = getTestExecutionContext();
        if (!properties.isOnlyFail() || !event.getTestResult().isSuccess()) {

            if (finishEventProcessorList != null) {
                finishEventProcessorList.forEach(p -> {
                    List<TestLogData> logData = p.processFinishEvent(testFinishEvent, testExecutionContext);
                    if (logData != null) {
                        testFinishEvent.getLogData().addAll(logData);
                    }
                });
            }
        }

        testFinishEvent.setData(testExecutionContext.getParams());
        eventsService.sendEvent(testFinishEvent);
    }

    /**
     * After class.
     *
     * @param event the event
     */
    @EventListener
    @Order(1)
    public void afterClass(ClassFinishEvent event) {
        TestExecutionContext testExecutionContext = getTestExecutionContext();
        testExecutionContext.clearContext();
    }

}
