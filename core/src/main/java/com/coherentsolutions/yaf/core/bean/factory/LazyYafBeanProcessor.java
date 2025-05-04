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

package com.coherentsolutions.yaf.core.bean.factory;


import com.coherentsolutions.yaf.core.bean.YafBean;
import com.coherentsolutions.yaf.core.bean.YafPrototype;
import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.utils.YafReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.stereotype.Component;

/**
 * This processor intercept bean processing, and if bean is page or utils - marks it lazy.
 */
@Component
public class LazyYafBeanProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // register threadLocal scope
        beanFactory.registerScope(Consts.SCOPE_THREADLOCAL, new SimpleThreadScope());
        for (String name : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            try {
                // todo validate this, may be not all beans appears in this alg
                if (beanDefinition instanceof ScannedGenericBeanDefinition) {
                    String className = beanDefinition.getBeanClassName();
                    Class cls = Class.forName(className);
                    if (YafReflectionUtils.isChild(cls, YafBean.class)) {
                        Lazy lazy = YafReflectionUtils.getAnnotation(cls, Lazy.class);
                        if (lazy == null || lazy.value()) {
                            beanFactory.getBeanDefinition(name).setLazyInit(true);
                        }
                    }
                    if (YafReflectionUtils.isChild(cls, com.coherentsolutions.yaf.core.pom.Component.class)) {
                        YafPrototype prototype = YafReflectionUtils.getAnnotation(cls, YafPrototype.class);
                        if (prototype != null && !prototype.makeSingleton()) {
                            beanFactory.getBeanDefinition(name).setScope(BeanDefinition.SCOPE_PROTOTYPE);
                        } else {
                            beanFactory.getBeanDefinition(name).setScope(Consts.SCOPE_THREADLOCAL);
                        }
                    }
                }
            } catch (Exception e) {
                //
            }
        }
    }
}
