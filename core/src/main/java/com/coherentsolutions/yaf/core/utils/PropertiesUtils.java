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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.StreamSupport;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * The type Properties utils.
 */
@Service
public class PropertiesUtils {

    /**
     * The Environment.
     */
    @Autowired
    Environment environment;

    /**
     * Gets property value.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the property value
     */
    public static String getPropertyValue(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (isEmpty(value)) {
            value = System.getenv(key);
            if (isEmpty(value)) {
                return defaultValue;
            }
        }
        return value;
    }

    /**
     * Gets all properties.
     *
     * @return the all properties
     */
    @Cacheable("properties")
    public Properties getAllProperties() {

        Properties props = new Properties();
        MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false).filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames()).flatMap(Arrays::stream)
                .forEach(propName -> props.setProperty(propName, environment.getProperty(propName)));
        props.putAll(System.getProperties());
        return props;
    }

}
