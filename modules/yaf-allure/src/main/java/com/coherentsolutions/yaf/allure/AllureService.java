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

import com.coherentsolutions.yaf.allure.writer.YafAllureResultsWriter;
import com.coherentsolutions.yaf.core.consts.Consts;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.listener.LifecycleNotifier;
import io.qameta.allure.listener.TestLifecycleListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * The type Allure service.
 */
@Service
@Slf4j
@Lazy(false)
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".allure.enabled", havingValue = "true")
public class AllureService {

    /**
     * The Lifecycle.
     */
    private final AllureLifecycle lifecycle;
    /**
     * The Allure properties.
     */
    @Getter
    private final AllureProperties allureProperties;

    private final TestPatcher testPatcher;

    private final List<YafAllureResultsWriter> writers;

    public AllureService(AllureProperties allureProperties, TestPatcher testPatcher, List<YafAllureResultsWriter> yafWriters) {
        this.allureProperties = allureProperties;
        this.testPatcher = testPatcher;
        this.writers = yafWriters;
        lifecycle = Allure.getLifecycle();
        // patch allure lifecycle
        try {
            Field notifierField = ReflectionUtils.findField(AllureLifecycle.class, "notifier");
            notifierField.setAccessible(true);
            LifecycleNotifier notifier = (LifecycleNotifier) notifierField.get(lifecycle);

            Field testListenersField = ReflectionUtils.findField(LifecycleNotifier.class, "testListeners");
            testListenersField.setAccessible(true);
            List<TestLifecycleListener> cn = (List<TestLifecycleListener>) testListenersField.get(notifier);
            cn.add(this.testPatcher);

            Field allureWriterField = ReflectionUtils.findField(AllureLifecycle.class, "writer");
            allureWriterField.setAccessible(true);
            AllureResultsWriter writer = (AllureResultsWriter) allureWriterField.get(lifecycle);
            yafWriters.forEach(w -> w.writeToResults(writer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
