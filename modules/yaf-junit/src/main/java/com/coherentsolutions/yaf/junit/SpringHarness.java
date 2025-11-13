package com.coherentsolutions.yaf.junit;

import org.springframework.test.context.TestContextManager;

import java.lang.reflect.Method;

class SpringHarness {
    private final TestContextManager manager;
    private final Class<?> testClass;

    SpringHarness(Class<?> testClass) {
        this.testClass = testClass;
        this.manager = new TestContextManager(testClass); // reads @SpringBootTest, etc.
    }

    void beforeAll() throws Exception {
        manager.beforeTestClass();
    }

    void prepare(Object testInstance) throws Exception {
        manager.prepareTestInstance(testInstance); // @Autowired fields, etc.
    }

    void beforeEach(Object testInstance, Method method) throws Exception {
        manager.beforeTestMethod(testInstance, method);
    }

    void afterEach(Object testInstance, Method method, Throwable thrown) throws Exception {
        manager.afterTestMethod(testInstance, method,
                (thrown == null) ? null : thrown);
    }

    void afterAll() throws Exception {
        manager.afterTestClass();
    }
}