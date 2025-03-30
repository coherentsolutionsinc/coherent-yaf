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

package com.coherentsolutions.yaf.web;


import com.coherentsolutions.yaf.core.events.EventsService;
import com.coherentsolutions.yaf.core.events.test.TestStartEvent;
import com.coherentsolutions.yaf.core.test.BaseYafTest;
import com.coherentsolutions.yaf.core.test.model.SuiteInfo;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.HashMap;

@Component
@Slf4j
public class JUnitListener implements TestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        EventsService eventsService = testContext.getApplicationContext().getBean(EventsService.class);
        TestStartEvent testStartEvent = new TestStartEvent();

        TestInfo testInfo = new TestInfo(testContext.getTestMethod(), null,
                annotations -> testContext.getTestMethod().getName());
        testInfo.setRunnerContext(testContext);
        testInfo.setTestParams(new HashMap<>());
        SuiteInfo suiteInfo = new SuiteInfo();
        suiteInfo.setSuiteName("Components Test Suite");

        testInfo.setSuiteInfo(suiteInfo);
//        testInfo.setEnvSetup(testInfo.getSuiteInfo().getSuiteParams().get(ENV_SETTING_PARAM));
        testStartEvent.setTestInfo(testInfo);
        testStartEvent.setTestClassInstance((BaseYafTest) testContext.getTestInstance());

        eventsService.sendEvent(testStartEvent);
        log.debug("Starting method {}", testInfo.getFullTestName());
    }
}
