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

package com.coherentsolutions.yaf.web.wait.driver;

import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The type Wait class utils.
 */
@Service
public class WaitClassUtils {

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    /**
     * Gets wait for fields.
     *
     * @param bean      the bean
     * @param predicate the predicate
     * @return the wait for fields
     */
    @Cacheable(cacheNames = "beanWaits", key = "{#bean.class, #predicate}")
    public Map<Object, WaitFor> getWaitForFields(Object bean, Predicate<WaitFor> predicate) {
        List<Field> classYafFields = beanUtils.getClassYafFields(this.getClass());
        Map<Object, WaitFor> result = new HashMap<>();
        for (Field f : classYafFields) {
            WaitFor waitFor = beanUtils.getAnnotation(f, WaitFor.class);
            if (predicate.test(waitFor)) {
                Object res = null;
                try {
                    res = f.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    // todo?
                }
                result.put(res, waitFor);
            }
        }
        return result;
    }

}
