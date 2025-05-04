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

package com.coherentsolutions.yaf.web.utils.component;

import com.coherentsolutions.yaf.core.bean.factory.YafBeanProcessor;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.web.pom.WebComponent;
import com.coherentsolutions.yaf.web.pom.by.SelectorType;
import com.coherentsolutions.yaf.web.pom.by.YafSetParam;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builder class for creating and configuring instances of {@link WebComponent}.
 * <p>
 * This builder class provides methods to set various properties and annotations
 * for a web component, and then build the component instance.
 * </p>
 *
 * @param <C> the type of the web component
 * @param <T> the type of the web element
 */
public class WebComponentBuilder<C extends WebComponent<T>, T extends WebElement> {

    private final YafBeanUtils beanUtils;
    private final YafBeanProcessor beanProcessor;
    private Class<C> componentClass;
    private By by;
    private T root;
    private final List<Annotation> annotations = new ArrayList<>();

    /**
     * Constructs a new {@code WebComponentBuilder} for the specified component class.
     *
     * @param componentClass the class of the web component to be built
     * @param beanUtils      the utility class for bean operations
     * @param beanProcessor  the processor for bean operations
     */
    public WebComponentBuilder(Class<C> componentClass, YafBeanUtils beanUtils, YafBeanProcessor beanProcessor) {
        this.componentClass = componentClass;
        this.beanUtils = beanUtils;
        this.beanProcessor = beanProcessor;
    }

    /**
     * Sets the component class for the builder.
     *
     * @param componentClass the class of the web component to be built
     * @return the current instance of {@code WebComponentBuilder}
     */
    public WebComponentBuilder<C, T> forClass(Class<C> componentClass) {
        this.componentClass = componentClass;
        return this;
    }

    /**
     * Sets the root element for the web component.
     *
     * @param root the root web element
     * @return the current instance of {@code WebComponentBuilder}
     */
    public WebComponentBuilder<C, T> rootElement(T root) {
        this.root = root;
        return this;
    }

    /**
     * Sets the root locator for the web component.
     *
     * @param by the locator for the root element
     * @return the current instance of {@code WebComponentBuilder}
     */
    public WebComponentBuilder<C, T> rootBy(By by) {
        this.by = by;
        return this;
    }

    /**
     * Adds YafSet parameters to the web component.
     *
     * @param yafSetParams a map of YafSet parameters
     * @return the current instance of {@code WebComponentBuilder}
     */
    public WebComponentBuilder<C, T> withYafSetParams(Map<String, String> yafSetParams) {
        yafSetParams.forEach((k, v) -> {
            YafSetParam yafSetParam = YafSetParamBuilder.builder().key(k).value(v).build();
            annotations.add(yafSetParam);
        });
        return this;
    }

    /**
     * Adds a single YafSet parameter to the web component.
     *
     * @param key   the key of the YafSet parameter
     * @param value the value of the YafSet parameter
     * @return the current instance of {@code WebComponentBuilder}
     */
    public WebComponentBuilder<C, T> withYafSetParam(String key, String value) {
        return withYafSetParams(Map.of(key, value));
    }

    /**
     * Adds a YafSet selector to the web component.
     *
     * @param field        the field to which the selector applies
     * @param value        the selector value
     * @param selectorType the type of the selector (optional)
     * @return the current instance of {@code WebComponentBuilder}
     */
    public WebComponentBuilder<C, T> withYafSetSelector(String field, String value, SelectorType... selectorType) {
        YafSetSelector yafSetSelector = YafSetSelectorBuilder.builder().field(field).value(value).type(selectorType.length > 0 ? selectorType[0] : SelectorType.CSS).build();
        annotations.add(yafSetSelector);
        return this;
    }

    /**
     * Builds and returns the configured web component instance.
     *
     * @return the configured instance of the web component
     */
    public C build() {
        TestExecutionContext testExecutionContext = beanUtils.getBean(TestExecutionContext.class);
        WebDriver webDriver = (WebDriver) testExecutionContext.getWebDriverHolder().getDriver();

        T finalRoot = root;
        if (by != null) {
            finalRoot = (T) webDriver.findElement(by);
        }

        C component = beanUtils.getBean(componentClass);
        component.setTestExecutionContext(testExecutionContext, null, finalRoot, null, annotations);
        beanProcessor.processBean(component, annotations, testExecutionContext);
        return component;
    }

}