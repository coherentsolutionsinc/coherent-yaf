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

import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.DataYafException;
import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.testng.dataprovider.DataProcessor;
import com.coherentsolutions.yaf.testng.dataprovider.model.Args;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;

/**
 * The interface Api data source.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
@Source(processor = ApiDataSource.Processor.class)
public @interface ApiDataSource {

    /**
     * Api call class.
     *
     * @return the class
     */
    Class apiCall();

    /**
     * The interface Api data call.
     */
    interface ApiDataCall {

        /**
         * Gets data.
         *
         * @param method               the method
         * @param annotation           the annotation
         * @param testExecutionContext the test execution context
         * @return the data
         * @throws GeneralYafException the general yaf exception
         */
        List<Args> getData(Method method, Annotation annotation, TestExecutionContext testExecutionContext)
                throws GeneralYafException;
    }

    /**
     * The type Processor.
     */
    @Component
    class Processor extends DataProcessor<Args> {

        /**
         * The Bean utils.
         */
        @Autowired
        YafBeanUtils beanUtils;

        @Override
        public Class getSupportedAnnotationClass() {
            return ApiDataSource.class;
        }

        @Override
        public List<Args> process(Method method, Annotation annotation, TestExecutionContext testExecutionContext)
                throws DataYafException {
            ApiDataSource source = (ApiDataSource) annotation;
            Class apiCall = source.apiCall();
            if (ApiDataCall.class.isAssignableFrom(apiCall)) {
                ApiDataCall dataCall = beanUtils.getBean(apiCall);
                try {
                    return dataCall.getData(method, annotation, testExecutionContext);
                } catch (GeneralYafException e) {
                    throw new DataYafException("Calling request error", e);
                }
            } else {
                throw new DataYafException("Unsupported ApiCall class specified " + apiCall.getName());
            }
        }
    }
}
