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

package com.coherentsolutions.yaf.testng.utils;

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

import com.coherentsolutions.yaf.core.enums.TestState;
import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import com.coherentsolutions.yaf.core.test.model.ClassInfo;
import com.coherentsolutions.yaf.core.test.model.SuiteInfo;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import com.coherentsolutions.yaf.core.test.model.TestResult;
import com.coherentsolutions.yaf.core.utils.YafReflectionUtils;
import com.coherentsolutions.yaf.testng.YafTransformer;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.coherentsolutions.yaf.core.consts.Consts.ENV_SETTING_PARAM;

/**
 * The type Test ng utils.
 */
public class TestNgUtils {

    /**
     * Build suite info suite info.
     *
     * @param testContext the test context
     * @return the suite info
     */
    public static SuiteInfo buildSuiteInfo(ITestContext testContext) {
        SuiteInfo suiteInfo = new SuiteInfo();
        ISuite suite = testContext.getSuite();
        suiteInfo.setSuiteName(suite.getName());
        suiteInfo.setSuiteParams(suite.getXmlSuite().getAllParameters());
        suiteInfo.setTestMethods(Arrays.stream(testContext.getAllTestMethods()).filter(m -> m.isTest())
                .map(m -> m.getConstructorOrMethod().getMethod()).collect(Collectors.toSet()));
        return suiteInfo;
    }

    /**
     * Build class info class info.
     *
     * @param testContext the test context
     * @param cls         the cls
     * @return the class info
     */
    public static ClassInfo buildClassInfo(ITestContext testContext, Class cls) {
        return new ClassInfo(cls);

    }

    /**
     * Build test info test info.
     *
     * @param testContext the test context
     * @param method      the method
     * @param methodArgs  the method args
     * @return the test info
     */
    public static TestInfo buildTestInfo(ITestContext testContext, Method method, Object[] methodArgs) {
        TestInfo testInfo = new TestInfo(method, methodArgs,
                annotations -> ((Test) YafReflectionUtils.findAnnotation(annotations, Test.class)).testName());
        testInfo.setTestParams(testContext.getCurrentXmlTest().getLocalParameters());
        testInfo.setRunnerContext(testContext);
        testInfo.setSuiteInfo(buildSuiteInfo(testContext));
        testInfo.setEnvSetup(testInfo.getSuiteInfo().getSuiteParams().get(ENV_SETTING_PARAM));
        return testInfo;
    }

    /**
     * Build test result test result.
     *
     * @param iTestResult the test result
     * @return the test result
     */
    public static TestResult buildTestResult(ITestResult iTestResult) {
        TestResult testResult = new TestResult();
        testResult.setSuccess(iTestResult.isSuccess());
        testResult.setExecutionTime(iTestResult.getEndMillis() - iTestResult.getStartMillis());
        testResult.setError(iTestResult.getThrowable());
        switch (iTestResult.getStatus()) {
            case 1: {
                testResult.setState(TestState.SUCCESS);
                break;
            }
            case 2: {
                testResult.setState(TestState.FAIL);
                break;
            }
            case 3: {
                testResult.setState(TestState.SKIP);
                break;
            }
            default: {
                testResult.setState(TestState.UNKNOWN);
                break;
            }
        }
        return testResult;

    }

    private static TestNG getTestNgInstance() throws GeneralYafException {
        try {
            Field field = TestNG.class.getDeclaredField("m_instance");
            field.setAccessible(true);
            TestNG testNG = (TestNG) field.get(null);
            return testNG;
        } catch (Exception e) {
            throw new GeneralYafException("Unable to get TestNG instance ! " + e.getMessage());
        }
    }

    /**
     * Add yaf listener.
     *
     * @throws GeneralYafException the general yaf exception
     */
    public static void addYafListener() throws GeneralYafException {
        try {
            TestNG testNG = getTestNgInstance();
            testNG.addListener(new YafTransformer());
        } catch (Exception e) {
            throw new GeneralYafException("Unable to patch TestNG listeners! " + e.getMessage());
        }
    }

    /**
     * Sets suite thread count.
     *
     * @param threadCount the thread count
     * @throws GeneralYafException the general yaf exception
     */
    public static void setSuiteThreadCount(int threadCount) throws GeneralYafException {
        try {
            TestNG testNG = getTestNgInstance();
            Field suiteThreads = TestNG.class.getDeclaredField("m_suiteThreadPoolSize");
            suiteThreads.setAccessible(true);
            suiteThreads.set(testNG, threadCount);
        } catch (Exception e) {
            throw new GeneralYafException("Unable to patch TestNG suite thread count! " + e.getMessage());
        }
    }

    /**
     * Clone and modify suite xml suite.
     *
     * @param initialSuite the initial suite
     * @param execConfig   the exec config
     * @return the xml suite
     */
    public static XmlSuite cloneAndModifySuite(XmlSuite initialSuite, String execConfig) {
        XmlSuite newSuite = new XmlSuite();
        newSuite.setName(initialSuite.getName() + "__" + execConfig);

        Map<String, String> newSuiteParams = new HashMap<>(initialSuite.getAllParameters());
        newSuiteParams.put(ENV_SETTING_PARAM, execConfig);
        newSuite.setParameters(newSuiteParams);

        newSuite.setListeners(initialSuite.getListeners());
        newSuite.setParallel(initialSuite.getParallel());
        newSuite.setThreadCount(initialSuite.getThreadCount());

        // iterate through tests
        List<XmlTest> newTests = initialSuite.getTests().stream().map(t -> {
            // System.out.println("----- " + t.getLocalParameters());
            XmlTest test = new XmlTest();
            test.setName(t.getName() + "--" + execConfig);

            Map<String, String> newTestParams = new HashMap<>(t.getLocalParameters());
            // newTestParams.putAll(newSuiteParams);
            // newTestParams.put(ENV_SETTING_PARAM, execConfig);

            test.setParameters(newTestParams);

            List<XmlClass> newClasses = t.getClasses().stream().map(c -> {
                XmlClass cc = new XmlClass(c.getName());
                cc.setXmlTest(test);
                return cc;
            }).collect(Collectors.toList());

            test.setClasses(newClasses);

            List<XmlPackage> packages = t.getPackages().stream().map(p -> {
                XmlPackage pp = new XmlPackage(p.getName());

                return pp;
            }).collect(Collectors.toList());
            test.setPackages(packages);

            test.setXmlSuite(newSuite);

            // System.out.println("--444--- " + test.getLocalParameters());

            return test;
        }).collect(Collectors.toList());

        newSuite.setTests(newTests);

        System.out.println(newSuite.toXml());

        return newSuite;
    }
}
