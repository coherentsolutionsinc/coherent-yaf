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

package com.coherentsolutions.yaf.selenide;

/*-
 * #%L
 * Yaf Web Module
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

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.coherentsolutions.yaf.core.bean.field.YafFieldPostProcessor;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.BeanInitYafException;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.web.pom.shadow.ShadowElement;
import com.coherentsolutions.yaf.web.pom.shadow.ShadowWebComponent;
import com.coherentsolutions.yaf.web.pom.shadow.ShadowWebPage;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.coherentsolutions.yaf.selenide.SelenideUtils.getSelector;

/**
 * The type Selenide shadow page post processing.
 */
@Service
public class SelenideShadowPagePostProcessing implements YafFieldPostProcessor {

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    @Override
    public boolean canPostProcess(Field field, Class fieldType, Object fieldValue, Object obj, Class objType,
                                  List<Annotation> annotations, TestExecutionContext testExecutionContext) {
        return ShadowWebPage.class.isAssignableFrom(fieldType);
    }

    @Override
    public Object postProcessField(Field field, Class fieldType, Object fieldValue, Object obj, Class objType,
                                   List<Annotation> annotations, TestExecutionContext testExecutionContext) throws BeanInitYafException {

        ShadowWebComponent component = (ShadowWebComponent) fieldValue;

        ReflectionUtils.doWithFields(fieldType, pageField -> {
            ShadowElement shadowElement = beanUtils.getAnnotation(pageField, ShadowElement.class);
            if (shadowElement != null && (pageField.getType().equals(SelenideElement.class)
                    || pageField.getType().equals(ElementsCollection.class))) {
                pageField.setAccessible(true);
                By selector = getSelector(component, AnnotationUtils.findAnnotation(pageField, ShadowElement.class));
                Class<?> pageFieldType = pageField.getType();
                Object value = null;
                if (pageFieldType.equals(SelenideElement.class)) {
                    value = $(selector);
                } else if (pageFieldType.equals(ElementsCollection.class)) {
                    value = $$(selector);
                }
                pageField.set(component, value);
            }
        });

        return fieldValue;
    }
}
