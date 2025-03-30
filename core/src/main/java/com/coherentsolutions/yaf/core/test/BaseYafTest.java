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

package com.coherentsolutions.yaf.core.test;


import com.coherentsolutions.yaf.core.bean.factory.YafBeanProcessor;
import com.coherentsolutions.yaf.core.context.IContextual;
import com.coherentsolutions.yaf.core.context.execution.ExecutionContext;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.events.EventsService;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The type Base yaf test.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public abstract class BaseYafTest implements IContextual {

    /**
     * The Application context.
     */
    @Autowired
    @Getter
    protected ApplicationContext applicationContext;

    /**
     * The Execution context.
     */
    @Autowired
    @Getter
    protected ExecutionContext executionContext;

    /**
     * The Test execution context.
     */
    @Autowired
    @Getter
    @Setter
    protected TestExecutionContext testExecutionContext;

    /**
     * The Events service.
     */
    @Autowired
    protected EventsService eventsService;
    /**
     * The Faker.
     */
    @Autowired
    protected Faker faker;
//    /**
//     * The Context field post processor.
//     */
//    @Autowired
//    YafContextFieldPostProcessor contextFieldPostProcessor;
    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;
    @Autowired
    private YafBeanProcessor beanProcessor;

    /**
     * Init test fields.
     */
// @PostConstruct
    public void initTestFields() {
        beanProcessor.processBean(this, beanUtils.getAnnotations(this.getClass()), testExecutionContext);
    }

//    /**
//     * Gets page.
//     *
//     * @param <T>       the type parameter
//     * @param pageClass the page class
//     * @return the page
//     * @throws BeanInitYafException the bean init yaf exception
//     */
//    protected <T extends Page> T getPage(Class pageClass) throws BeanInitYafException {
//        return getObject(pageClass);
//    }
//
//    /**
//     * Gets object.
//     *
//     * @param <T>         the type parameter
//     * @param pageClass   the page class
//     * @param annotations the annotations
//     * @return the object
//     * @throws BeanInitYafException the bean init yaf exception
//     */
//    protected <T> T getObject(Class pageClass, List<Annotation>... annotations) throws BeanInitYafException {
//        return (T) contextFieldPostProcessor.processField(null, pageClass, null, null,
//                annotations.length > 0 ? annotations[0] : Collections.emptyList(), testExecutionContext);
//    }

    /**
     * Gets web driver.
     *
     * @param name the name
     * @return the web driver
     */
    protected DriverHolder getWebDriver(String... name) {
        return testExecutionContext.getWebDriverHolder(name);
    }

    /**
     * Gets mobile driver.
     *
     * @param name the name
     * @return the mobile driver
     */
    protected DriverHolder getMobileDriver(String... name) {
        return testExecutionContext.getMobileDriverHolder(name);
    }

    /**
     * Gets desktop driver.
     *
     * @param name the name
     * @return the desktop driver
     */
    protected DriverHolder getDesktopDriver(String... name) {
        return testExecutionContext.getDesktopDriverHolder(name);
    }

    @Override
    public void setTestExecutionContext(TestExecutionContext testExecutionContext, Field field, Object obj,
                                        Class objType, List<Annotation> annotations) {

        log.info("Setting TEC for {} [{}]", this.getClass().getName(), Thread.currentThread().getName());
        this.testExecutionContext = testExecutionContext;
    }
}
