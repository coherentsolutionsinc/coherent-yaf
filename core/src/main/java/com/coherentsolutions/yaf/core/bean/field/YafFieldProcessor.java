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

package com.coherentsolutions.yaf.core.bean.field;


import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.BeanInitYafException;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The interface Yaf field processor.
 */
@Component
public interface YafFieldProcessor {

    /**
     * Can process boolean.
     *
     * @param field                the field
     * @param fieldType            the field type
     * @param obj                  the obj
     * @param objType              the obj type
     * @param annotations          the annotations
     * @param testExecutionContext the test execution context
     * @return the boolean
     */
    boolean canProcess(Field field, Class fieldType, Object obj, Class objType, List<Annotation> annotations,
                       TestExecutionContext testExecutionContext);

    /**
     * Process field object.
     *
     * @param field                the field
     * @param fieldType            the field type
     * @param obj                  the obj
     * @param objType              the obj type
     * @param annotations          the annotations
     * @param testExecutionContext the test execution context
     * @return the object
     * @throws BeanInitYafException the bean init yaf exception
     */
    Object processField(Field field, Class fieldType, Object obj, Class objType, List<Annotation> annotations,
                        TestExecutionContext testExecutionContext) throws BeanInitYafException;
}
