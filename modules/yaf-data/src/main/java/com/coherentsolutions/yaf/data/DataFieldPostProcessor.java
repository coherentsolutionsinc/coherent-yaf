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


import com.coherentsolutions.yaf.core.bean.field.YafFieldProcessor;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.BeanInitYafException;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.data.reader.DataReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The type Data field post processor.
 */
@Service
@Order(1)
@Slf4j
public class DataFieldPostProcessor implements YafFieldProcessor {

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    @Override
    public boolean canProcess(Field field, Class fieldType, Object obj, Class objType, List<Annotation> annotations,
                              TestExecutionContext testExecutionContext) {
        return beanUtils.getAnnotation(fieldType, YafData.class) != null;
    }

    @Override
    public Object processField(Field field, Class fieldType, Object obj, Class objType, List<Annotation> annotations,
                               TestExecutionContext testExecutionContext) throws BeanInitYafException {
        YafData data = beanUtils.getAnnotation(fieldType, YafData.class);
        Object value = ReflectionUtils.getField(field, obj);
        if (value == null || data.alwaysNew()) { //field not inited or we need the new instance
            try {
                DataReader dataReader = beanUtils.getBean(data.reader());
                return dataReader.readData(data, fieldType, testExecutionContext);
            } catch (Exception e) {
                throw new BeanInitYafException(e.getMessage(), e);
            }
        }
        return value;
    }
}
