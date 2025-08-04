package com.coherentsolutions.yaf.allure;

import org.springframework.stereotype.Service;

/**
 * The type Allure tests store.
 */
@Service
public class AllureTestsStore {

    private final ThreadLocal<YafTestContainer> yafTest = new ThreadLocal<>();

    /**
     * Gets test container.
     *
     * @return the test container
     */
    public YafTestContainer getTestContainer() {
        return yafTest.get();
    }

    /**
     * Sets test container.
     *
     * @param testContainer the test container
     */
    public void setTestContainer(YafTestContainer testContainer) {
        yafTest.set(testContainer);
    }

    /**
     * Clear test container.
     */
    public void clearTestContainer() {
        yafTest.remove();
    }
}
