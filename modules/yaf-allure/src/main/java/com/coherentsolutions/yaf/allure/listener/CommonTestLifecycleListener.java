/*
 * MIT License
 *
 * Copyright (c) 2021 - 2025 Coherent Solutions Inc.
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

package com.coherentsolutions.yaf.allure.listener;

import com.coherentsolutions.yaf.allure.AllureProperties;
import com.coherentsolutions.yaf.allure.AllureService;
import com.coherentsolutions.yaf.allure.AllureTestsStore;
import com.coherentsolutions.yaf.allure.YafTestContainer;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.events.test.TestStartEvent;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

/**
 * The type Common test lifecycle listener.
 */
@Service
@Slf4j
@ConditionalOnBean(AllureService.class)
//@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".allure.enabled", havingValue = "true")
public class CommonTestLifecycleListener {

    /**
     * The Allure tests store.
     */
    @Autowired
    AllureTestsStore allureTestsStore;

    /**
     * The Allure properties.
     */
    @Autowired
    AllureProperties allureProperties;

    /**
     * Test start.
     *
     * @param testStartEvent the test start event
     */
    @EventListener
    public void testStart(final TestStartEvent testStartEvent) {
        allureTestsStore.setTestContainer(new YafTestContainer(testStartEvent.getTestInfo()));
    }

    /**
     * Test end.
     *
     * @param testFinishEvent the test finish event
     */
    @EventListener
    public void testEnd(final TestFinishEvent testFinishEvent) {
        // append attachments
        if (allureProperties.isFullLogAllTests() || !testFinishEvent.getTestResult().isSuccess()) {
            YafTestContainer container = allureTestsStore.getTestContainer();
            if (container != null) {
                container.setTestResult(testFinishEvent.getTestResult());
                testFinishEvent.getLogData().forEach(ld -> {
                    try {
                        Allure.addAttachment(ld.getLogDataName(), ld.getContentType(),
                                new ByteArrayInputStream(ld.getData()), ld.getFileExt());
                    } catch (Exception e) {
                        log.error("Unable to append log data attach cause " + e.getMessage(), e);
                    }
                });
            }
        }
    }
}
