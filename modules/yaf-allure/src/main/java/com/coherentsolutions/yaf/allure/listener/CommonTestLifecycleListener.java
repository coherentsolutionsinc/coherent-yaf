package com.coherentsolutions.yaf.allure.listener;

import com.coherentsolutions.yaf.allure.AllureProperties;
import com.coherentsolutions.yaf.allure.AllureTestsStore;
import com.coherentsolutions.yaf.allure.YafTestContainer;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.events.test.TestStartEvent;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

/**
 * The type Common test lifecycle listener.
 */
@Service
@Slf4j
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
