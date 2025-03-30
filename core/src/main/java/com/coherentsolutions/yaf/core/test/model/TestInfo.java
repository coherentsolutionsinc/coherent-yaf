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

package com.coherentsolutions.yaf.core.test.model;


import com.coherentsolutions.yaf.core.test.YafTest;
import com.coherentsolutions.yaf.core.utils.YafReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Test info.
 */
@Accessors(chain = true)
public class TestInfo {

    /**
     * The Test id.
     */
    @Getter
    String testId;
    /**
     * The Test name.
     */
    @Getter
    String testName;
    /**
     * The Test method name.
     */
    @Getter
    String testMethodName;

    /**
     * The Suite info.
     */
    @Getter
    @Setter
    SuiteInfo suiteInfo;

    /**
     * The Full test name.
     */
    String fullTestName;
    /**
     * The Test method.
     */
    @Getter
    Method testMethod;
    /**
     * The Test class.
     */
    @Getter
    Class testClass;
    /**
     * The Test method params.
     */
    @Getter
    @Setter
    Map<String, Object> testMethodParams;
    /**
     * The Test params.
     */
    @Getter
    @Setter
    Map<String, String> testParams;

    /**
     * The Annotations.
     */
    @Getter
    List<Annotation> annotations;

    /**
     * The Yaf test.
     */
    @Getter
    YafTest yafTest;
    /**
     * The Env setup.
     */
    @Getter
    @Setter
    String envSetup;
    /**
     * The Runner context.
     */
    @Getter
    @Setter
    Object runnerContext;

    /**
     * Instantiates a new Test info.
     *
     * @param method                 the method
     * @param methodArgs             the method args
     * @param vendorTestNameResolver the vendor test name resolver
     */
    public TestInfo(Method method, Object[] methodArgs, Function<List<Annotation>, String>... vendorTestNameResolver) {
        testId = UUID.randomUUID().toString();
        testMethod = method;
        testMethodName = method.getName();
        testClass = method.getDeclaringClass();
        annotations = YafReflectionUtils.getAnnotations(method);

        yafTest = YafReflectionUtils.findAnnotation(annotations, YafTest.class);
        if (yafTest != null && yafTest.name() != null) {
            testName = yafTest.name();
        } else {
            for (Function<List<Annotation>, String> resolver : vendorTestNameResolver) {
                testName = resolver.apply(annotations);
                if (testName != null) {
                    break;
                }
            }
            if (testName != null) {
                testName = method.getName();
            }
        }
        if (methodArgs != null && methodArgs.length > 0) {
            testMethodParams = new HashMap<>();
            List<String> methodParamsNames = Arrays.stream(method.getParameters()).map(p -> p.getName())
                    .collect(Collectors.toList());
            for (int i = 0; i < methodParamsNames.size(); i++) {
                String paramName = methodParamsNames.get(i);
                Object paramValue = methodArgs[i];
                testMethodParams.put(paramName, paramValue);
            }
        }
    }

    /**
     * Gets full test name.
     *
     * @return the full test name
     */
    public String getFullTestName() {
        if (fullTestName == null) {
            fullTestName = testClass.getName() + "." + testMethodName + "[" + testName + "]";
        }
        return fullTestName;
    }

}
