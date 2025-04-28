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

package com.coherentsolutions.yaf.testng;

/*-
 * #%L
 * Yaf TestNG Module
 * %%
 * Copyright (C) 2020 - 2021 CoherentSolutions
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.coherentsolutions.yaf.core.test.BaseYafTest;
import com.coherentsolutions.yaf.core.utils.SkipReport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.EventPublishingTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is a fork of org.springframework.test.context.testng.AbstractTestNGSpringContextTests with custom
 * modifications
 */
@TestExecutionListeners({ServletTestExecutionListener.class, DirtiesContextBeforeModesTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        EventPublishingTestExecutionListener.class})
abstract class BaseTestNgTest extends BaseYafTest implements IHookable {

    // spring support (add BeforeSuite for BeforeClass methods to add context to beforeSuite methods

    /**
     * Logger available to subclasses.
     */
    protected final Log log = LogFactory.getLog(getClass());

    private final TestContextManager testContextManager;

    private Throwable testException;

    /**
     * Construct a new {@code AbstractTestNGSpringContextTests} instance and initialize the internal
     * {@link TestContextManager} for the current test class.
     */
    BaseTestNgTest() {
        this.testContextManager = new TestContextManager(getClass());
    }

    /**
     * Delegates to the configured {@link TestContextManager} to call {@linkplain TestContextManager#beforeTestClass()
     * 'before test class'}*** callbacks.
     *
     * @throws Exception if a registered TestExecutionListener throws an exception
     */
    @SkipReport
    @BeforeSuite
    @BeforeClass(alwaysRun = true)
    protected void springTestContextBeforeTestClass() throws Exception {
        this.testContextManager.beforeTestClass();
    }

    /**
     * Delegates to the configured {@link TestContextManager} to
     * {@linkplain TestContextManager#prepareTestInstance(Object) prepare} this test instance prior to execution of any
     * individual tests, for example for injecting dependencies, etc.
     *
     * @throws Exception if a registered TestExecutionListener throws an exception
     */
    @SkipReport
    @BeforeSuite
    @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextBeforeTestClass")
    protected void springTestContextPrepareTestInstance() throws Exception {
        this.testContextManager.prepareTestInstance(this);
    }

    /**
     * Delegates to the configured {@link TestContextManager} to
     * {@linkplain TestContextManager#beforeTestMethod(Object, Method) pre-process} the test method before the actual
     * test is executed.
     *
     * @param testMethod the test method which is about to be executed
     * @throws Exception allows all exceptions to propagate
     */
    @SkipReport
    @BeforeMethod(alwaysRun = true)
    protected void springTestContextBeforeTestMethod(Method testMethod) throws Exception {
        this.testContextManager.beforeTestMethod(this, testMethod);
    }

    /**
     * Delegates to the {@linkplain IHookCallBack#runTestMethod(ITestResult) test method} in the supplied
     * {@code callback} to execute the actual test and then tracks the exception thrown during test execution, if any.
     *
     * @see org.testng.IHookable#run(IHookCallBack, ITestResult)
     */
    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        Method testMethod = testResult.getMethod().getConstructorOrMethod().getMethod();
        boolean beforeCallbacksExecuted = false;

        try {
            this.testContextManager.beforeTestExecution(this, testMethod);
            beforeCallbacksExecuted = true;
        } catch (Throwable ex) {
            this.testException = ex;
        }

        if (beforeCallbacksExecuted) {
            callBack.runTestMethod(testResult);
            this.testException = getTestResultException(testResult);
        }

        try {
            this.testContextManager.afterTestExecution(this, testMethod, this.testException);
        } catch (Throwable ex) {
            if (this.testException == null) {
                this.testException = ex;
            }
        }

        if (this.testException != null) {
            throwAsUncheckedException(this.testException);
        }
    }

    /**
     * Delegates to the configured {@link TestContextManager} to
     * {@linkplain TestContextManager#afterTestMethod(Object, Method, Throwable) post-process} the test method after the
     * actual test has executed.
     *
     * @param testMethod the test method which has just been executed on the test instance
     * @throws Exception allows all exceptions to propagate
     */
    @SkipReport
    @AfterMethod(alwaysRun = true)
    protected void springTestContextAfterTestMethod(Method testMethod) throws Exception {
        try {
            this.testContextManager.afterTestMethod(this, testMethod, this.testException);
        } finally {
            this.testException = null;
        }
    }

    /**
     * Delegates to the configured {@link TestContextManager} to call {@linkplain TestContextManager#afterTestClass()
     * 'after test class'}*** callbacks.
     *
     * @throws Exception if a registered TestExecutionListener throws an exception
     */
    @SkipReport
    @AfterClass(alwaysRun = true)
    protected void springTestContextAfterTestClass() throws Exception {
        this.testContextManager.afterTestClass();
    }

    private Throwable getTestResultException(ITestResult testResult) {
        Throwable testResultException = testResult.getThrowable();
        if (testResultException instanceof InvocationTargetException) {
            testResultException = ((InvocationTargetException) testResultException).getCause();
        }
        return testResultException;
    }

    private RuntimeException throwAsUncheckedException(Throwable t) {
        throwAs(t);
        // Appeasing the compiler: the following line will never be executed.
        throw new IllegalStateException(t);
    }

    @SuppressWarnings("unchecked")
    private <T extends Throwable> void throwAs(Throwable t) throws T {
        throw (T) t;
    }

}
