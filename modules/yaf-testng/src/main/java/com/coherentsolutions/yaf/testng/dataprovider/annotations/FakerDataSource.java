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

package com.coherentsolutions.yaf.testng.dataprovider.annotations;

/*-
 * #%L
 * Yaf TestNG Module
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

import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.DataYafException;
import com.coherentsolutions.yaf.testng.dataprovider.DataProcessor;
import com.coherentsolutions.yaf.testng.dataprovider.model.Args;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.METHOD;

/**
 * The interface Faker data source.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
@Source(processor = FakerDataSource.Processor.class)
public @interface FakerDataSource {

    /**
     * Value string.
     *
     * @return the string
     */
    String value();

    /**
     * Size int.
     *
     * @return the int
     */
    int size() default 2;

    /**
     * The type Processor.
     */
    @Component
    class Processor extends DataProcessor<Args> {

        /**
         * The Methods cache.
         */
        static Map<String, Method> methodsCache = new ConcurrentHashMap<>();

        @Override
        public Class getSupportedAnnotationClass() {
            return FakerDataSource.class;
        }

        @Override
        public List<Args> process(Method method, Annotation annotation, TestExecutionContext testExecutionContext)
                throws DataYafException {
            FakerDataSource source = (FakerDataSource) annotation;
            return generateArgs(source.value(), source.size(),
                    (Locale) testExecutionContext.getParam(Consts.CTX_LOCALE));
        }

        private List<Args> generateArgs(String fakerString, int size, Locale locale) throws DataYafException {
            Faker faker = locale != null ? new Faker(locale) : new Faker();
            List<String> methods = parseFakerMethods(fakerString);
            if (!methods.isEmpty()) {
                List<Args> result = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    result.add(getFakeData(methods, faker));
                }
                return result;
            } else {
                throw new DataYafException("Please specify a valid Faker methods chain!");
            }
        }

        private Args getFakeData(List<String> methods, Faker faker) throws DataYafException {
            Object res = faker;
            for (String methodName : methods) {
                res = invoke(res, methodName);
            }
            return Args.build(res);
        }

        private Object invoke(Object source, String methodName) throws DataYafException {
            try {
                Method method = methodsCache.get(methodName);
                if (method == null) {
                    method = source.getClass().getMethod(methodName);
                    methodsCache.put(methodName, method);
                }
                return method.invoke(source);
            } catch (Exception ex) {
                throw new DataYafException("Unable to invoke " + methodName + " on " + source, ex);
            }
        }

        private List<String> parseFakerMethods(String fakerString) {
            if (fakerString != null) {
                fakerString = fakerString.replace("faker.", "");
            }
            if (StringUtils.isNotEmpty(fakerString)) {
                return Arrays.stream(fakerString.split("\\.")).map(s -> s.replace("()", ""))
                        .collect(Collectors.toList());
            }
            return null;
        }
    }
}
