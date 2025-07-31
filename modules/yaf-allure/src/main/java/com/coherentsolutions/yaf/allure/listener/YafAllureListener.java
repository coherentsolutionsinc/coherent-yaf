package com.coherentsolutions.yaf.allure.listener;

import com.coherentsolutions.yaf.allure.YafTestContainer;

import java.util.Map;

/**
 * The interface Yaf allure listener.
 */
public interface YafAllureListener {

    /**
     * Sets test store.
     *
     * @param testStore the test store
     */
    void setTestStore(Map<String, YafTestContainer> testStore);
}
