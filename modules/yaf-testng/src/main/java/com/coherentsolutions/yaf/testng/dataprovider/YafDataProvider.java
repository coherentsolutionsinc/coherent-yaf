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

package com.coherentsolutions.yaf.testng.dataprovider;

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

import com.coherentsolutions.yaf.core.exception.DataYafException;
import com.coherentsolutions.yaf.core.utils.SafeParse;
import com.coherentsolutions.yaf.core.utils.StaticContextAccessor;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.testng.dataprovider.annotations.Source;
import com.coherentsolutions.yaf.testng.dataprovider.model.Args;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.Parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type Yaf data provider.
 */
public class YafDataProvider {

    /**
     * Data object [ ] [ ].
     *
     * @param m            the m
     * @param iTestContext the test context
     * @return the object [ ] [ ]
     * @throws DataYafException the data yaf exception
     */
    public static Object[][] data(Method m, ITestContext iTestContext) throws DataYafException {
        int methodParamsCount = m.getParameterCount();

        if (methodParamsCount > 0) {

            List<Args> results = invokeCustomDataProvider(m);

            int processedParams = 0;
            // try to detect how many params were populated by custom data provider
            if (results != null && !results.isEmpty()) {
                processedParams = results.get(0).length();
            }

            if (methodParamsCount > processedParams) {
                // try to get that params from suite file or ...
                Args methodParams = getMethodParams(m, iTestContext);
                if (methodParams != null) {
                    if (processedParams == 0) {
                        // we have not got any dataprovider or it return empty list, try to populate from suite params
                        results = Arrays.asList(methodParams);
                    } else {
                        // add method arg to each dataprovider arg
                        results.forEach(a -> a.addAllArgs(methodParams.getArgs()));
                    }
                    processedParams += methodParams.length();
                }
            }
            // one more double check, does we prepare all params
            if (methodParamsCount > processedParams) {
                // here we should check all method params types, and if they are injected via testng by default - its
                // ok, if not - raise an exception
            }

            return convertArgsList(results);

        }
        return new Object[1][0];
    }

    /**
     * Convert args list object [ ] [ ].
     *
     * @param args the args
     * @return the object [ ] [ ]
     */
    protected static Object[][] convertArgsList(List<Args> args) {
        Object[][] result = new Object[1][0];
        if (args != null && !args.isEmpty() && args.get(0).length() > 0) {

            result = new Object[args.size()][args.get(0).length()];
            for (int i = 0; i < args.size(); i++) {
                Args a = args.get(i);
                for (int j = 0; j < a.length(); j++) {
                    result[i][j] = a.getArgs().get(j);
                }
            }
        }
        return result;
    }

    /**
     * Invoke custom data provider list.
     *
     * @param method the method
     * @return the list
     * @throws DataYafException the data yaf exception
     */
    protected static List<Args> invokeCustomDataProvider(Method method) throws DataYafException {
        List<Args> result = new ArrayList<>();
        Annotation dataAnnotation = Arrays.stream(method.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(Source.class)).findFirst()
                .orElse(null);
        if (dataAnnotation != null) {
            Source sourceAnnotation = dataAnnotation.annotationType().getAnnotation(Source.class);
            if (sourceAnnotation != null) {
                YafBeanUtils yafBeanUtils = StaticContextAccessor.getBean(YafBeanUtils.class);
                DataProcessor dataProcessor = yafBeanUtils.getBean(sourceAnnotation.processor());
                result.addAll(dataProcessor.processData(method, dataAnnotation, yafBeanUtils.tec()));
                return result;
            }
        }
        return null;
    }

    /**
     * Gets method params.
     *
     * @param method       the method
     * @param iTestContext the test context
     * @return the method params
     */
    protected static Args getMethodParams(Method method, ITestContext iTestContext) {
        Parameters parameters = method.getAnnotation(Parameters.class);
        if (parameters != null) {
            String[] names = parameters.value();
            Map<String, String> testParams = ((TestRunner) iTestContext).getTest().getAllParameters();
            Args result = new Args();
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < names.length; i++) {
                Class type = parameterTypes[i];
                String stringValue = testParams.get(names[i]);
                if (type.equals(String.class)) {
                    result.addArg(stringValue);
                } else if (type.equals(int.class)) {
                    result.addArg(SafeParse.parseInt(stringValue));
                } else if (type.equals(boolean.class)) {
                    result.addArg(SafeParse.parseBoolean(stringValue));
                }
                // TODO add other data types
            }
            return result;
        }
        return null;
    }

}
