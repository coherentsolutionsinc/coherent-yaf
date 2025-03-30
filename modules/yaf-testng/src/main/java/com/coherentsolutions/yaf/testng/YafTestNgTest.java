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

import com.coherentsolutions.yaf.core.events.global.SuiteStartEvent;
import com.coherentsolutions.yaf.core.events.test.*;
import com.coherentsolutions.yaf.core.exception.DataYafException;
import com.coherentsolutions.yaf.core.test.model.ClassInfo;
import com.coherentsolutions.yaf.core.test.model.SuiteInfo;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import com.coherentsolutions.yaf.core.utils.SkipReport;
import com.coherentsolutions.yaf.testng.dataprovider.YafDataProvider;
import com.coherentsolutions.yaf.testng.utils.TestNgUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * The type Yaf test ng test.
 */
@Slf4j
public abstract class YafTestNgTest extends BaseTestNgTest {

    /**
     * The constant DATA.
     */
// custom dataProvider
    public static final String DATA = "data";

    /**
     * Data object [ ] [ ].
     *
     * @param m            the m
     * @param iTestContext the test context
     * @return the object [ ] [ ]
     * @throws DataYafException the data yaf exception
     */
    @DataProvider
    public static Object[][] data(Method m, ITestContext iTestContext) throws DataYafException {
        return YafDataProvider.data(m, iTestContext);
    }

    /**
     * Before suite.
     *
     * @param testContext the test context
     */
    @SkipReport
    @BeforeSuite
    public void beforeSuite(ITestContext testContext) {
        SuiteStartEvent suiteStartEvent = new SuiteStartEvent();
        SuiteInfo suiteInfo = TestNgUtils.buildSuiteInfo(testContext);
        suiteStartEvent.setSuiteInfo(suiteInfo);
        eventsService.sendEvent(suiteStartEvent);
        log.debug("Starting suite {}", suiteInfo.getSuiteName());
    }

    /**
     * Before class.
     *
     * @param testContext the test context
     */
    @SkipReport
    @BeforeClass
    public void beforeClass(ITestContext testContext) {
        ClassStartEvent classStartEvent = new ClassStartEvent();
        ClassInfo classInfo = TestNgUtils.buildClassInfo(testContext, this.getClass());
        classStartEvent.setClassInfo(classInfo);
        classStartEvent.setTestClassInstance(this);
        eventsService.sendEvent(classStartEvent);
        log.debug("Starting class {}", classInfo.getClassName());
    }

    /**
     * After class.
     *
     * @param testContext the test context
     */
    @SkipReport
    @AfterClass (alwaysRun = true)
    public void afterClass(ITestContext testContext) {
        ClassFinishEvent classStopEvent = new ClassFinishEvent();
        ClassInfo classInfo = TestNgUtils.buildClassInfo(testContext, this.getClass());
        classStopEvent.setClassInfo(classInfo);
        eventsService.sendEvent(classStopEvent);
        log.debug("Stopping class {}", classInfo.getClassName());
    }

    /**
     * Before method.
     *
     * @param testContext the test context
     * @param method      the method
     * @param methodArgs  the method args
     */
// @SkipReport
    @BeforeMethod
    public void beforeMethod(ITestContext testContext, Method method, Object[] methodArgs) {
        TestStartEvent testStartEvent = new TestStartEvent();

        TestInfo testInfo = TestNgUtils.buildTestInfo(testContext, method, methodArgs);
        testStartEvent.setTestInfo(testInfo);
        testStartEvent.setTestClassInstance(this);

        eventsService.sendEvent(testStartEvent);
        log.debug("Starting method {}", testInfo.getFullTestName());

    }

    // @SkipReport
    // @BeforeMethod(dependsOnMethods = "beforeMethod")
    // public void initTestClassObjects(ITestContext testContext) {
    // initTestFields();//TODO validate post construct
    // log.info("Initiated for test {}", testContext.getName());
    // }

    /**
     * After test method.
     *
     * @param testContext the test context
     * @param method      the method
     * @param methodArgs  the method args
     * @param testResult  the test result
     */
// we could not skip this method, we need to process this event before lifecycle is closed
    @AfterMethod
    public void afterTestMethod(ITestContext testContext, Method method, Object[] methodArgs, ITestResult testResult) {
        RawTestFinishEvent testFinishEvent = new RawTestFinishEvent();
        testFinishEvent.setTestInfo(testExecutionContext.getTestInfo());
        testFinishEvent.setTestResult(TestNgUtils.buildTestResult(testResult));

        eventsService.sendEvent(testFinishEvent);
        log.debug("After method {}", testExecutionContext.getTestInfo().getFullTestName());
    }

}
