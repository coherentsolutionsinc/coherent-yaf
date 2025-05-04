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

package com.coherentsolutions.yaf.allure.testng;

/*-
 * #%L
 * Yaf Allure TestNG Module
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

import com.coherentsolutions.yaf.core.utils.SkipReport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.testng.IInvokedMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Allure test ng skip aspect.
 */
@Aspect
public class AllureTestNgSkipAspect {

    /**
     * The Cache methods.
     */
    Map<Method, Boolean> cacheMethods = new ConcurrentHashMap<>();

    /**
     * Before.
     */
    @Pointcut("execution(* io.qameta.allure.testng.AllureTestNg.beforeInvocation(..))")
    public void before() {
        // pointcut body, should be empty
    }

    /**
     * After.
     */
    @Pointcut("execution(* io.qameta.allure.testng.AllureTestNg.afterInvocation(..))")
    public void after() {
        // pointcut body, should be empty
    }

    /**
     * Skip aspect.
     *
     * @param joinPoint the join point
     * @throws Throwable the throwable
     */
    @Around("before() || after() ")
    public void skipAspect(final ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((IInvokedMethod) joinPoint.getArgs()[0]).getTestMethod().getConstructorOrMethod().getMethod();
        if (!skip(method)) {
            try {
                joinPoint.proceed();
            } catch (Throwable e) {
                throw e;
            }
        }
    }

    /**
     * Skip boolean.
     *
     * @param method the method
     * @return the boolean
     */
    public boolean skip(Method method) {
        Boolean skip = cacheMethods.get(method);
        if (skip == null) {
            SkipReport skipReport = AnnotationUtils.findAnnotation(method, SkipReport.class);
            skip = skipReport != null;
            cacheMethods.put(method, skip);
        }
        return skip;
    }
}
