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

import com.coherentsolutions.yaf.testng.utils.EnvSkipTest;
import com.coherentsolutions.yaf.testng.utils.YafDefaultRetryAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.DisabledRetryAnalyzer;
import org.testng.internal.annotations.IAnnotationTransformer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * The type Yaf annotation transformer.
 */
@Slf4j
public class YafAnnotationTransformer implements IAnnotationTransformer {

    /**
     * The Enable retry.
     */
    boolean enableRetry;

    String currentProfile;

    /**
     * Instantiates a new Yaf annotation transformer.
     */
    public YafAnnotationTransformer() {
        enableRetry = System.getProperty("retry") != null;
        currentProfile = System.getProperty("spring.profiles.active");
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod, Class<?> occurringClazz) {
        if (annotation.getDataProvider().isEmpty() && testMethod.getParameterCount() > 0) {
            log.debug("Appending custom data provider for {} in {}", testMethod.getName(),
                    testMethod.getDeclaringClass().getName());
            annotation.setDataProviderClass(YafTestNgTest.class);
            annotation.setThreadPoolSize(0);
            annotation.setDataProvider(YafTestNgTest.DATA);
        }
        if (enableRetry && DisabledRetryAnalyzer.class.equals(annotation.getRetryAnalyzerClass())) {
            log.debug("Appending retry analyzer for {} in {}", testMethod.getName(),
                    testMethod.getDeclaringClass().getName());
            annotation.setRetryAnalyzer(YafDefaultRetryAnalyzer.class);
        }
        EnvSkipTest envTest = AnnotationUtils.getAnnotation(testMethod, EnvSkipTest.class);
        if (envTest != null && currentProfile != null && envTest.skip().length > 0) {
            boolean skipTest = Arrays.stream(envTest.skip()).anyMatch(env -> currentProfile.contains(env));
            if (skipTest) {
                annotation.setEnabled(false);
            }
        }
    }
}
