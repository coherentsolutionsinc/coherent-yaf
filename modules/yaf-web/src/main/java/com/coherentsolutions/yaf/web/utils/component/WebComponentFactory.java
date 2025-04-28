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
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.web.pom.WebComponent;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Factory class for creating instances of {@link WebComponentBuilder}.
 * <p>
 * This factory class provides methods to create and configure instances of
 * {@link WebComponentBuilder} for building web components.
 * </p>
 */
@Service
public class WebComponentFactory {

    /**
     * The Bean utils.
     */
    @Autowired
    protected YafBeanUtils beanUtils;
    /**
     * The Bean processor.
     */
    @Autowired
    YafBeanProcessor beanProcessor;

    /**
     * Creates a new {@code WebComponentBuilder} for the specified component class.
     *
     * @param <C>            the type of the web component
     * @param <T>            the type of the web element
     * @param componentClass the class of the web component to be built
     * @return a new instance of {@code WebComponentBuilder} for the specified component class
     */
    public <C extends WebComponent<T>, T extends WebElement> WebComponentBuilder<C, T> createComponent(Class<C> componentClass) {
        return new WebComponentBuilder<>(componentClass, beanUtils, beanProcessor);
    }
}