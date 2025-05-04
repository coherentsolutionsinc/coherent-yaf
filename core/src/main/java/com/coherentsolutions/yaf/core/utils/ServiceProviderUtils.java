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

package com.coherentsolutions.yaf.core.utils;

import com.coherentsolutions.yaf.core.exec.DefaultExecutionService;
import com.coherentsolutions.yaf.core.exec.ExecutionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

/**
 * The type Service provider utils.
 */
@Slf4j
public class ServiceProviderUtils {


    /**
     * The constant servicesList.
     */
    protected static Map<Class, List<?>> servicesList = new HashMap<>();
    /**
     * The constant singletonServicesList.
     */
    protected static Map<Class, List<?>> singletonServicesList = new HashMap<>();

    /**
     * Gets services.
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return the services
     */
    public static <T> List<T> getServices(Class cls) {
        List<T> res = (List<T>) servicesList.get(cls);
        if (res != null) {
            return res;
        }
        ServiceLoader<T> cdLoader = ServiceLoader.load(cls);
        List<T> services = new ArrayList<>();
        cdLoader.iterator().forEachRemaining(c -> services.add(c));
        servicesList.put(cls, services);
        return services;
    }

    /**
     * Gets single services.
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return the single services
     */
    public static <T> T getSingleServices(Class cls) {
        return (T) getServices(cls).stream().findFirst().orElse(null);
    }

    /**
     * Gets singleton services.
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return the singleton services
     */
    @SneakyThrows
    public static <T> List<T> getSingletonServices(Class cls) {
        List<T> res = (List<T>) singletonServicesList.get(cls);
        if (res != null) {
            return res;
        }
        ClassLoader classLoader = ServiceProviderUtils.class.getClassLoader();
        List<String> serviceNames = getServiceNames(classLoader, cls.getName());
        List<T> services = new ArrayList<>();
        for (String serviceName : serviceNames) {
            Class<?> singletonClass = Class.forName(serviceName);
            Method getInstanceMethod = singletonClass.getMethod("getInstance");
            T singletonInstance = (T) getInstanceMethod.invoke(null);
            services.add(singletonInstance);
        }
        singletonServicesList.put(cls, services);
        return services;
    }

    /**
     * Gets single singleton services.
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return the single singleton services
     */
    public static <T> T getSingleSingletonServices(Class cls) {
        return (T) getSingletonServices(cls).stream().findFirst().orElse(null);
    }

    /**
     * Get execution service execution service.
     *
     * @return the execution service
     */
    public static ExecutionService getExecutionService() {
        ExecutionService executionService = ServiceProviderUtils.getSingleSingletonServices(ExecutionService.class);
        if (executionService == null) {
            log.info("Unable to load Execution Service! Using default one.");
            executionService = DefaultExecutionService.getInstance();
        }
        return executionService;
    }


    private static List<String> getServiceNames(ClassLoader classLoader, String serviceClassName) throws Exception {
        List<String> serviceNames = new ArrayList<>();

        InputStream inputStream = classLoader.getResourceAsStream("META-INF/services/" + serviceClassName);
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                serviceNames.add(line.trim());
            }
            reader.close();
        }
        return serviceNames;
    }


}
