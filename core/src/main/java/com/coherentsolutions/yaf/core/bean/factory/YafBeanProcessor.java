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

package com.coherentsolutions.yaf.core.bean.factory;


import com.coherentsolutions.yaf.core.bean.field.YafFieldPostProcessor;
import com.coherentsolutions.yaf.core.bean.field.YafFieldProcessor;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.BeanInitYafException;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.core.utils.YafReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The type Yaf bean processor.
 */
@Service
@Slf4j
public class YafBeanProcessor {

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    /**
     * The Application context.
     */
    @Autowired
    ApplicationContext applicationContext;

    /**
     * The Field processors.
     */
    @Autowired(required = false)
    List<YafFieldProcessor> fieldProcessors;
    /**
     * The Field post processors.
     */
    @Autowired(required = false)
    List<YafFieldPostProcessor> fieldPostProcessors;

    /**
     * Process bean.
     *
     * @param bean                 the bean
     * @param annotations          the annotations
     * @param testExecutionContext the test execution context
     * @throws BeanInitYafException the bean init yaf exception
     */
    public void processBean(Object bean, List<Annotation> annotations, TestExecutionContext testExecutionContext)
            throws BeanInitYafException {
        Class beanType = bean.getClass();

        List<Field> fields = beanUtils.getClassYafFields(beanType);

        for (Field field : fields) {
            Class fieldType = field.getType();

            Object fieldValue = getFieldValue(field, fieldType, bean, beanType, annotations, testExecutionContext);

            if (fieldValue == null) {
                try {
                    fieldValue = field.get(bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            fieldValue = getPostProcessedFieldValue(field, fieldType, fieldValue, bean, beanType, annotations,
                    testExecutionContext);
            YafReflectionUtils.setFieldValue(field, fieldValue, bean);

        }
    }

    /**
     * Gets field value.
     *
     * @param field                the field
     * @param fieldType            the field type
     * @param bean                 the bean
     * @param beanType             the bean type
     * @param annotations          the annotations
     * @param testExecutionContext the test execution context
     * @return the field value
     * @throws BeanInitYafException the bean init yaf exception
     */
    public Object getFieldValue(Field field, Class fieldType, Object bean, Class beanType, List<Annotation> annotations,
                                TestExecutionContext testExecutionContext) throws BeanInitYafException {

        YafFieldProcessor fieldProcessor = fieldProcessors.stream()
                .filter(p -> p.canProcess(field, fieldType, bean, beanType, annotations, testExecutionContext))
                .findFirst().orElse(null);

        Object fieldValue = null;

        if (fieldProcessor != null) {
            fieldValue = fieldProcessor.processField(field, fieldType, bean, beanType, annotations,
                    testExecutionContext);
        }
        return fieldValue;
    }

    /**
     * Gets post processed field value.
     *
     * @param field                the field
     * @param fieldType            the field type
     * @param fieldValue           the field value
     * @param bean                 the bean
     * @param beanType             the bean type
     * @param annotations          the annotations
     * @param testExecutionContext the test execution context
     * @return the post processed field value
     * @throws BeanInitYafException the bean init yaf exception
     */
    public Object getPostProcessedFieldValue(Field field, Class fieldType, Object fieldValue, Object bean,
                                             Class beanType, List<Annotation> annotations, TestExecutionContext testExecutionContext)
            throws BeanInitYafException {

        Object finalValue = fieldValue; // just for lambda
        YafFieldPostProcessor fieldPostProcessor = fieldPostProcessors.stream().filter(
                        p -> p.canPostProcess(field, fieldType, finalValue, bean, beanType, annotations, testExecutionContext))
                .findFirst().orElse(null);

        if (fieldPostProcessor != null) {
            fieldValue = fieldPostProcessor.postProcessField(field, fieldType, finalValue, bean, beanType, annotations,
                    testExecutionContext);
        }
        return fieldValue;
    }
}
