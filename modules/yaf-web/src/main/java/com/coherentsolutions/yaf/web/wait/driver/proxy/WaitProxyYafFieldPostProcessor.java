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

package com.coherentsolutions.yaf.web.wait.driver.proxy;

import com.coherentsolutions.yaf.core.bean.field.YafFieldPostProcessor;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.pom.Component;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.web.wait.driver.BaseWait;
import com.coherentsolutions.yaf.web.wait.driver.DriverWaitService;
import com.coherentsolutions.yaf.web.wait.driver.WaitFor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The type Wait proxy yaf field post processor.
 */
@Slf4j
@Service
public class WaitProxyYafFieldPostProcessor implements YafFieldPostProcessor {

    /**
     * The Wait service.
     */
    @Autowired
    DriverWaitService waitService;

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    /**
     * Gets wait annotation.
     *
     * @param field the field
     * @return the wait annotation
     */
    protected WaitFor getWaitAnnotation(Field field) {
        return beanUtils.getAnnotation(field, WaitFor.class);
    }

    @Override
    public boolean canPostProcess(Field field, Class fieldType, Object fieldValue, Object obj, Class objType,
                                  List<Annotation> annotations, TestExecutionContext testExecutionContext) {
        return beanUtils.isChild(fieldType, Component.class) && getWaitAnnotation(field) != null;
    }

    @Override
    public Object postProcessField(Field field, Class fieldType, Object fieldValue, Object obj, Class objType,
                                   List<Annotation> annotations, TestExecutionContext testExecutionContext) {
        WaitFor waitForAnnotation = getWaitAnnotation(field);

        // get waitFor child annotation
        Annotation waitAnnotation = Arrays.stream(field.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(WaitFor.class)).findFirst()
                .orElse(null);
        if (waitAnnotation == null) {
            // it basic wait for annotation
            waitAnnotation = waitForAnnotation;
        }

        ProxyFactory factory = new ProxyFactory(fieldValue);
        Component component = (Component) obj;
        WebDriver driver = (WebDriver) component.getDriverHolder().getDriver();
        factory.addAdvice(new WebElementAdvice(waitForAnnotation, waitAnnotation, driver));
        return factory.getProxy();
    }

    /**
     * The type Web element advice.
     */
    public class WebElementAdvice implements MethodInterceptor {

        /**
         * The Already loaded.
         */
        boolean alreadyLoaded;
        /**
         * The Wait.
         */
        Annotation wait;
        /**
         * The Wait for.
         */
        WaitFor waitFor;
        /**
         * The Driver.
         */
        WebDriver driver;

        /**
         * Instantiates a new Web element advice.
         *
         * @param waitFor the wait for
         * @param wait    the wait
         * @param driver  the driver
         */
        public WebElementAdvice(WaitFor waitFor, Annotation wait, WebDriver driver) {
            this.wait = wait;
            this.waitFor = waitFor;
            this.driver = driver;
        }

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            if (!alreadyLoaded || waitFor.waitEveryTime()) {
                // wait for element according condition
                Object target = methodInvocation.getThis();
                List<WebElement> elements;
                if (target instanceof List) {
                    elements = (List<WebElement>) target;
                } else {
                    WebElement element = (WebElement) target;
                    elements = Collections.singletonList(element);
                }
                BaseWait baseWait = (BaseWait) waitFor.waitClass()
                        .getConstructor(WaitFor.class, Annotation.class, List.class)
                        .newInstance(waitFor, wait, elements);
                waitService.waitFor(baseWait);
            }
            return methodInvocation.proceed();
        }

    }

    // @Autowired(required = false)
    // List<YafBeanFieldProxyService> proxyServiceList;
    //
    // @Override
    // public boolean canPostProcess(Field field, Class fieldType, Object fieldValue, Object obj, Class objType,
    // List<Annotation> annotations, TestExecutionContext testExecutionContext) {
    // return proxyServiceList != null;
    // }
    //
    // @Override
    // public Object postProcessField(Field field, Class fieldType, Object fieldValue, Object obj, Class objType,
    // List<Annotation> annotations, TestExecutionContext testExecutionContext) {
    // YafBeanFieldProxyService yafFieldProxy = proxyServiceList.stream()
    // .filter(p -> p.canProxy(field)).findFirst().orElse(null);
    // if (yafFieldProxy != null) {
    // return yafFieldProxy.proxyField(field, fieldValue, obj);
    // }
    // return fieldValue;
    // }
}
