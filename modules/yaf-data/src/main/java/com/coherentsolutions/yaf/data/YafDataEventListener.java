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

package com.coherentsolutions.yaf.data;

import com.coherentsolutions.yaf.core.events.test.ClassStartEvent;
import com.coherentsolutions.yaf.core.test.BaseYafTest;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.core.utils.YafReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Yaf data event listener.
 */
@Service
public class YafDataEventListener {

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    /**
     * The Field post processor.
     */
    @Autowired
    DataFieldPostProcessor fieldPostProcessor;

    /**
     * Before class.
     *
     * @param event the event
     */
    @EventListener
    @Order(1)
    public void beforeClass(ClassStartEvent event) {

        //init data fields
        BaseYafTest bean = event.getTestClassInstance();
        Class beanType = bean.getClass();

        List<Field> fields = beanUtils.getClassYafFields(beanType);

        for (Field field : fields) {
            Class fieldType = field.getType();

            if (fieldPostProcessor.canProcess(field, fieldType, bean, beanType, new ArrayList<>(), null)) {
                Object fieldValue = fieldPostProcessor.processField(field, fieldType, bean, beanType, new ArrayList<>(), null);
                if (fieldValue != null) {
                    YafReflectionUtils.setFieldValue(field, fieldValue, bean);
                }
            }
        }
    }
}
