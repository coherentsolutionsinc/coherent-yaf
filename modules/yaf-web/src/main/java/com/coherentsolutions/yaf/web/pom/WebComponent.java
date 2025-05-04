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

package com.coherentsolutions.yaf.web.pom;

import com.coherentsolutions.yaf.core.bean.YafPrototype;
import com.coherentsolutions.yaf.core.bean.factory.YafBeanProcessor;
import com.coherentsolutions.yaf.core.utils.YafReflectionUtils;
import com.coherentsolutions.yaf.web.pom.by.NotInheritedRoot;
import com.coherentsolutions.yaf.web.pom.by.YafSetParam;
import com.coherentsolutions.yaf.web.pom.by.YafSetParams;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;
import com.coherentsolutions.yaf.web.pom.factory.StubLocatorFactoryForRoot;
import com.coherentsolutions.yaf.web.pom.factory.YafNotPageFactory;
import com.coherentsolutions.yaf.web.utils.YafByUtils;
import com.coherentsolutions.yaf.web.utils.by.YafBy;
import com.coherentsolutions.yaf.web.utils.component.WebComponentBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type N web component.
 *
 * @param <T> the type parameter
 */
@org.springframework.stereotype.Component
@YafPrototype
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public abstract class WebComponent<T extends WebElement> extends GeneralWebYafObject implements RootedWebComponent<T> {

    private static final String ROOT = "root";

    /**
     * The Bean processor.
     */
    @Autowired
    YafBeanProcessor beanProcessor;

    @Override
    protected void init(Field field, Object obj, Class fieldType, List<Annotation> annotations, String driverName) {
        super.init(field, obj, fieldType, annotations, driverName);
        //detect if there are any selectors overwrite from parent file, and if any - overwrite them
        Map<String, YafSetSelector> overwriteMap = YafByUtils.parseFieldAnnotations(annotations);
        List<YafSetParam> globalSetParam = annotations.stream().filter(YafSetParam.class::isInstance).map(YafSetParam.class::cast).collect(Collectors.toList());
        annotations.stream().filter(YafSetParams.class::isInstance).map(YafSetParams.class::cast).forEach(yafSetParams -> globalSetParam.addAll(List.of(yafSetParams.value())));
        locatorFactory.setYafSetParam(globalSetParam);
        // field is null when initializing object using createWebComponent method
        ReflectionUtils.doWithFields(field != null ? field.getType() : this.getClass(), field1 -> {
            YafSetSelector yafBySelector = overwriteMap.get(field1.getName());
            if (yafBySelector != null) {
                ReflectionUtils.makeAccessible(field1);
                ReflectionUtils.setField(field1, this, YafByUtils.getFromSetSelector(yafBySelector));
            }
        }, field1 -> field1.getType().equals(YafBy.class));

        if (properties.isUsePageFactory() && this.getClass().getAnnotation(YafNotPageFactory.class) == null) {
            locatorFactory.setWebDriver(getWebDriver());
            try {
                Field root = ReflectionUtils.findField(this.getClass(), ROOT);
                if (root != null) {
                    Field parentRoot = obj != null ? ReflectionUtils.findField(obj.getClass(), ROOT) : null;
                    YafBy parentFindBy = (field != null) ? YafByUtils.convert(YafReflectionUtils.getAnnotation(field, FindBy.class)) : null;
                    NotInheritedRoot parentNotInheritedRoot = (field != null) ? YafReflectionUtils.getAnnotation(field, NotInheritedRoot.class) : null;
                    NotInheritedRoot thisNotInheritedRoot = (field != null) ? YafReflectionUtils.getAnnotation(root, NotInheritedRoot.class) : null;
                    YafBy thisBy = YafByUtils.getYafByFromField(root, overwriteMap.get(ROOT));

                    if (!globalSetParam.isEmpty()) {
                        thisBy = YafByUtils.parametrise(thisBy, YafByUtils.convertYafSetParamToMap(globalSetParam));
                    }

                    if (obj instanceof WebComponent<?> && !(obj instanceof WebPage<?>) && parentRoot != null) {
                        if (parentNotInheritedRoot == null && thisNotInheritedRoot == null) {
                            // WebComponent inside another WebComponent
                            locatorFactory.setParent((WebComponent<?>) obj);
                            locatorFactory.setParentYafBy(parentFindBy != null ? parentFindBy : thisBy);
                        } else {
                            // WebComponent with @NotInheritedRoot inside another WebComponent
                            setupRootWithLocator(parentFindBy != null ? parentFindBy : thisBy, root);
                        }
                    } else if (obj instanceof WebElement) {
                        // WebComponent without @FindBy on root element, created via createWebComponent method with setting rootElement/rootBy
                        setRoot((T) obj);
                    } else {
                        setupRootWithLocator(parentFindBy != null ? parentFindBy : thisBy, root);
                    }
                }
            } catch (Exception ex) {
                log.warn(ex.getMessage());
            }
            locatorFactory.setAnnotationMap(overwriteMap);
            locatorFactory.setRoot(getRoot());
            PageFactory.initElements(locatorFactory, this);
        }
    }

    /**
     * Setup root element with locator.
     */
    private void setupRootWithLocator(YafBy rootBy, Field root) {
        ElementLocatorFactory defFactory = new StubLocatorFactoryForRoot(getWebDriver(), rootBy.getBy());
        DefaultFieldDecorator decorator = new DefaultFieldDecorator(defFactory);
        Object rootWebElement = decorator.decorate(this.getClass().getClassLoader(), root);
        setRoot((T) rootWebElement);
    }

    /**
     * Creates a new `WebComponentBuilder` for the specified component class.
     * <p>
     * This method initializes a `WebComponentBuilder` for the given component class,
     * allowing for the construction and configuration of web components.
     * </p>
     *
     * @param <C>            the type of the web component
     * @param componentClass the class of the web component to be built
     * @return a new `WebComponentBuilder` for the specified component class
     */
    public <C extends WebComponent<T>> WebComponentBuilder<C, T> createWebComponentBuilder(Class<C> componentClass) {
        return new WebComponentBuilder<>(componentClass, beanUtils, beanProcessor);
    }

    /**
     * Get page p.
     *
     * @param <P> the type parameter
     * @param cls the cls
     * @return the p
     */
    public <P extends WebPage> P getPage(Class cls) {
        P page = beanUtils.getYafManagedBean(cls);
        return page;
    }

}