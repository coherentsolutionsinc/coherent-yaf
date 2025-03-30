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

import com.coherentsolutions.yaf.core.utils.YafReflectionUtils;
import com.coherentsolutions.yaf.web.pom.by.YafSet;
import com.coherentsolutions.yaf.web.pom.by.YafSetParam;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;
import com.coherentsolutions.yaf.web.utils.by.YafBy;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coherentsolutions.yaf.web.pom.by.SelectorType.*;

/**
 * Utility class for handling YafBy operations.
 * <p>
 * This class provides methods to parse annotations, convert FindBy annotations,
 * and parameterize YafBy instances.
 * </p>
 */
public class YafByUtils {

    /**
     * Parses a list of annotations and extracts YafSetSelector annotations.
     *
     * @param annotations the list of annotations to parse
     * @return a map of field names to YafSetSelector instances
     */
    public static Map<String, YafSetSelector> parseFieldAnnotations(List<Annotation> annotations) {
        Map<String, YafSetSelector> result = new HashMap<>();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.equals(YafSetSelector.class)) {
                YafSetSelector yafSetSelector = (YafSetSelector) annotation;
                result.put(yafSetSelector.field(), yafSetSelector);
            } else if (aClass.equals(YafSet.class)) {
                YafSet yafSet = (YafSet) annotation;
                for (YafSetSelector yafSetSelector : yafSet.value()) {
                    result.put(yafSetSelector.field(), yafSetSelector);
                }
            }
        }
        return result;
    }

    /**
     * Converts a YafSetSelector instance to a YafBy instance.
     *
     * @param yafSetSelector the YafSetSelector instance to convert
     * @return a YafBy instance
     */
    public static YafBy getFromSetSelector(YafSetSelector yafSetSelector) {
        return yafSetSelector != null ?
                new YafBy(yafSetSelector.type(), yafSetSelector.value()) : null;
    }

    /**
     * Converts a FindBy annotation to a YafBy instance.
     *
     * @param findBy the FindBy annotation to convert
     * @return a YafBy instance
     */
    //TODO add support of findBys and FindAll
    public static YafBy convert(FindBy findBy) {
        if (findBy != null) {
            if (!"".equals(findBy.css())) {
                return new YafBy(CSS, findBy.css());
            } else if (!"".equals(findBy.xpath())) {
                return new YafBy(XPATH, findBy.xpath());
            } else if (!"".equals(findBy.id())) {
                return new YafBy(ID, findBy.id());
            } else if (!"".equals(findBy.linkText())) {
                return new YafBy(LINK_TEXT, findBy.linkText());
            } else if (!"".equals(findBy.name())) {
                return new YafBy(NAME, findBy.name());
            } else if (!"".equals(findBy.className())) {
                return new YafBy(CLASS_NAME, findBy.className());
            } else if (!"".equals(findBy.tagName())) {
                return new YafBy(TAG_NAME, findBy.tagName());
            }
        }
        return null;
    }

    /**
     * Parameterizes a YafBy instance with a map of parameters.
     *
     * @param initialYafBy the initial YafBy instance
     * @param params       the map of parameters
     * @return the parameterized YafBy instance
     */
    public static YafBy parametrise(YafBy initialYafBy, Map<String, String> params) {
        if (initialYafBy != null) {
            String selector = initialYafBy.getSelector();
            if (selector.contains("${") && params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    selector = selector.replace("${" + entry.getKey() + "}", entry.getValue());
                }
            }
            initialYafBy.setSelector(selector);
        }
        return initialYafBy;
    }

    /**
     * Converts a list of YafSetParam annotations to a map.
     *
     * @param yafSetParams the list of YafSetParam annotations
     * @return a map of keys to values
     */
    public static Map<String, String> convertYafSetParamToMap(List<YafSetParam> yafSetParams) {
        Map<String, String> params = new HashMap<>();
        for (YafSetParam e : yafSetParams) {
            params.put(e.key(), e.value());
        }
        return params;
    }

    /**
     * Gets a YafBy instance from a field and a YafSetSelector.
     *
     * @param field       the field to inspect
     * @param setSelector the YafSetSelector to use
     * @return the corresponding YafBy instance
     */
    public static YafBy getYafByFromField(Field field, YafSetSelector setSelector) {
        if (setSelector != null && !setSelector.value().isEmpty()) {
            return getFromSetSelector(setSelector);
        }
        FindBy findBy = YafReflectionUtils.getAnnotation(field, FindBy.class);
        return setSelector != null && !setSelector.value().isEmpty() ?
                new com.coherentsolutions.yaf.web.utils.by.YafBy(setSelector.type(), setSelector.value()) :
                YafByUtils.convert(findBy);
    }

    public static YafBy getYafByFromField(Field field) {
        return getYafByFromField(field, (YafSetSelector) null);
    }

    /**
     * Gets a YafBy instance from a field and a list of YafSetParam annotations.
     *
     * @param field       the field to inspect
     * @param yafSetParam the list of YafSetParam annotations
     * @return the corresponding YafBy instance
     */
    public static YafBy getYafByFromField(Field field, List<YafSetParam> yafSetParam) {
        FindBy findBy = YafReflectionUtils.getAnnotation(field, FindBy.class);
        return YafByUtils.parametrise(YafByUtils.convert(findBy), convertYafSetParamToMap(yafSetParam));
    }
}