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

package com.coherentsolutions.yaf.core.pom;


import com.coherentsolutions.yaf.core.bean.YafBean;
import com.coherentsolutions.yaf.core.context.IContextual;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The type Component.
 */
@YafComponent
@Slf4j
//@org.springframework.stereotype.Component
public abstract class Component implements IContextual, YafBean {

    /**
     * The Test execution context.
     */
    @Getter
    protected TestExecutionContext testExecutionContext;

    /**
     * The Driver holder.
     */
    @Getter
    protected DriverHolder driverHolder;

    /**
     * The Bean utils.
     */
//    @Autowired
//    protected DriverWaitService waitService;
    @Autowired
    protected YafBeanUtils beanUtils;
//    @Autowired
//    private WaitClassUtils waitClassUtils;

    /**
     * The Context field post processor.
     */
//    @Autowired
//    YafContextFieldPostProcessor contextFieldPostProcessor;
    @Override
    public void setTestExecutionContext(TestExecutionContext testExecutionContext, Field field, Object obj,
                                        Class objectType, List<Annotation> annotations) {
        this.testExecutionContext = testExecutionContext;
        String driverName = null;
        if (annotations != null) {
            Driver d = (Driver) annotations.stream().filter(a -> a instanceof Driver).findFirst().orElse(null);
            if (d == null && objectType != null) {
                d = beanUtils.getAnnotation(objectType, Driver.class);
            }
            if (d != null) {
                driverName = d.value();
            }
        }
        init(field, obj, objectType, annotations, driverName);
    }

    /**
     * Is loaded boolean.
     *
     * @return the boolean
     */
    public boolean isLoaded() {
        try {
            waitForLoad();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Wait for load.
     */
    public void waitForLoad() {
        //TODO 112
    }
//            throws WebDriverException {
//        Map<Object, WaitFor> waitForFieldsForLoad = waitClassUtils.getWaitForFields(this, (w) -> w.isForLoad());
//        if (!waitForFieldsForLoad.isEmpty()) {
//            waitForFieldsForLoad.keySet().forEach(e -> {
//                WebElement element = null;
//                if (e instanceof List) {
//                    List<WebElement> elements = (List<WebElement>) e;
//                    // todo check
//                    // seems that we already should wait all elements and calling one item will be enough
//                    if (!elements.isEmpty()) {
//                        element = elements.get(0);
//                    }
//                } else {
//                    element = (WebElement) e;
//                }
//                if (element != null) {
//                    element.getTagName();// just trigger proxy
//                } else {
//                    throw new WebDriverException("xxxxxx");
//                }
//            });
//        }
//    }

    /**
     * Init.
     *
     * @param field       the field
     * @param obj         the obj
     * @param fieldType   the field type
     * @param annotations the annotations
     * @param driverName  the driver name
     */
    protected abstract void init(Field field, Object obj, Class fieldType, List<Annotation> annotations,
                                 String driverName);

    /**
     * Configure component.
     */
    public void configureComponent() {
    }

//    protected YafDeviceBy __(DeviceType deviceType, YafBy by) {
//        return new YafDeviceBy(testExecutionContext, driverHolder).add(deviceType, by);
//    }
//
//    protected YafBy _$(String selector) {
//        return YafBy.css(selector);
//    }
//
//    protected YafBy _x(String selector) {
//        return YafBy.xpath(selector);
//    }
//
//    protected YafBy _$(SelectorType type, String selector) {
//        return new YafBy(type, selector);
//    }

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
}
