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

package com.coherentsolutions.yaf.web.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * The type Pom generic utils.
 */
public class PomGenericUtils {

    /**
     * Gets second generic type.
     *
     * @param cls the cls
     * @return the second generic type
     */
    public static Class getSecondGenericType(Class cls) {
        Type type = cls.getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class) paramType.getActualTypeArguments()[1];
    }

    /**
     * Create componenet t.
     *
     * @param <T>          the type parameter
     * @param cls          the cls
     * @param yafWebHolder the yaf web holder
     * @return the t
     */
//    @SneakyThrows
//    public static <T extends WebPlainComponent> T createComponenet(Class cls, YafWebHolder yafWebHolder) {
//        Constructor<?> constructor = cls.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        WebPlainComponent wpc = (WebPlainComponent) constructor.newInstance();
//        wpc.setYafWebHolder(yafWebHolder);
//        YafPageFactory ypf = YafReflectionUtils.getAnnotation(cls, YafPageFactory.class);
//        if (ypf != null) {
//            wpc.initFactory(yafWebHolder, wpc);
//        }
//        return (T) wpc;
//    }
}
