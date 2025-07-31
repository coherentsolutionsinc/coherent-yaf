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

import com.coherentsolutions.yaf.allure.apicall.ApiCallAllureAttachmentProcessor;
import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.events.test.ApiCallStartEvent;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.events.test.TestStartEvent;
import com.coherentsolutions.yaf.core.test.YafTest;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.listener.LifecycleNotifier;
import io.qameta.allure.listener.TestLifecycleListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Allure service.
 */
@Service
@Slf4j
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".allure.enabled", havingValue = "true")
public class AllureService {

    /**
     * The Allure properties.
     */
    @Getter
    private final AllureProperties allureProperties;
    /**
     * The Test store.
     */
    private final Map<String, YafTest> testStore = new ConcurrentHashMap<>();
    /**
     * The Lifecycle.
     */
    private AllureLifecycle lifecycle;

    private TestPatcher testPatcher;

    private ApiCallAllureAttachmentProcessor apiCallAllureAttachmentProcessor;

    /**
     * Instantiates a new Allure service.
     *
     * @param allureProperties                 the allure properties
     * @param testPatcher                      the test patcher
     * @param apiCallAllureAttachmentProcessor the api call allure attachment processor
     */
    public AllureService(final AllureProperties allureProperties, final TestPatcher testPatcher, ApiCallAllureAttachmentProcessor apiCallAllureAttachmentProcessor) {
        this.allureProperties = allureProperties;
        this.testPatcher = testPatcher;
        this.testPatcher.setTestStore(testStore);
        lifecycle = Allure.getLifecycle();
        this.apiCallAllureAttachmentProcessor = apiCallAllureAttachmentProcessor;

        // TODO add description why we need such complex solution

        // patch allure lifecycle
        try {
            Field f = ReflectionUtils.findField(AllureLifecycle.class, "notifier");
            f.setAccessible(true);
            LifecycleNotifier notifier = (LifecycleNotifier) f.get(lifecycle);

            Field ff = ReflectionUtils.findField(LifecycleNotifier.class, "testListeners");
            ff.setAccessible(true);
            List<TestLifecycleListener> cn = (List<TestLifecycleListener>) ff.get(notifier);
            cn.add(this.testPatcher);

            Field wr = ReflectionUtils.findField(AllureLifecycle.class, "writer");
            wr.setAccessible(true);
            AllureResultsWriter writer = (AllureResultsWriter) wr.get(lifecycle);
            if (allureProperties.getEnvMap() != null) {
                Properties properties = mapToProperties(allureProperties.getEnvMap());
                writer.write("environment.properties", propertiesToInputStream(properties));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Map to properties properties.
     *
     * @param map the map
     * @return the properties
     */
    public static Properties mapToProperties(final Map<String, String> map) {
        Properties properties = new Properties();
        map.forEach((key, value) -> properties.setProperty(key, value));
        return properties;
    }

    /**
     * Properties to input stream input stream.
     *
     * @param properties the properties
     * @return the input stream
     * @throws Exception the exception
     */
    public static InputStream propertiesToInputStream(final Properties properties) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        properties.store(outputStream, null);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * Test start.
     *
     * @param testStartEvent the test start event
     */
    @EventListener
    // @Async
    public void testStart(final TestStartEvent testStartEvent) {

        YafTest yafTest = testStartEvent.getTestInfo().getYafTest();
        if (yafTest != null) {
            TestInfo testInfo = testStartEvent.getTestInfo();
            testStore.put(testInfo.getTestClass().getName() + "." + testInfo.getTestMethodName(), yafTest);
        }

    }

    /**
     * ApiCall start.
     *
     * @param apiCallStartEvent the api call start event
     */
    @EventListener
    // @Async
    public void apiCallStart(final ApiCallStartEvent apiCallStartEvent) {
        apiCallAllureAttachmentProcessor.setAttachmentsFromApiCallLog(apiCallStartEvent.getApiCallLog());
    }

    /**
     * Test end.
     *
     * @param testFinishEvent the test finish event
     */
    @EventListener
    // @Async // todo validate async>
    public void testEnd(final TestFinishEvent testFinishEvent) {
        // append attachments
        if (allureProperties.isFullLogAllTests() || !testFinishEvent.getTestResult().isSuccess()) {
            lifecycle.getCurrentTestCaseOrStep().ifPresent(parentUuid -> {

                // process attachments

                testFinishEvent.getLogData().forEach(ld -> {
                    try {
                        lifecycle.addAttachment(ld.getLogDataName(), ld.getContentType(), ld.getFileExt(),
                                ld.getData());
                    } catch (Exception e) {
                        log.error("Unable to append log data attach cause " + e.getMessage(), e);
                    }
                });
            });
        }
        AllureCategoriesUtil.copyCategoriesJsonToAllureResults(allureProperties.getCategoriesJsonFilePath());
    }
}
