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



import com.coherentsolutions.yaf.core.bean.Yaf;
import com.coherentsolutions.yaf.core.bean.factory.YafBeanProcessor;
import com.coherentsolutions.yaf.core.condition.ConditionMatchService;
import com.coherentsolutions.yaf.core.context.IContextual;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.EnvSetupYafException;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Yaf bean utils.
 */
@Service
public class YafBeanUtils {

    /**
     * The Supported classes.
     */
    @Autowired
    @Qualifier("supportedClasses")
    protected List<Class> supportedClasses;

    /**
     * The Application context.
     */
    @Autowired
    ApplicationContext applicationContext;

    /**
     * The Match service.
     */
    @Autowired
    ConditionMatchService matchService;

    /**
     * Gets class yaf fields.
     *
     * @param cls the cls
     * @return the class yaf fields
     */
    @Cacheable(value = "yafBeanFields", key = "#cls")
    public List<Field> getClassYafFields(Class cls) {
        List<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(cls, field -> {
            field.setAccessible(true);
            fields.add(field);
        }, field -> AnnotationUtils.findAnnotation(field.getType(), Yaf.class) != null
                || AnnotationUtils.findAnnotation(field, Yaf.class) != null
                || isSupportedBean(field.getType()));
        return fields;
    }

    /**
     * Gets class fields.
     *
     * @param cls            the cls
     * @param supportedTypes the supported types
     * @return the class fields
     */
    @Cacheable(value = "beanFields", key = "{#cls, #supportedTypes}")
    public Map<String, Field> getClassFields(Class cls, Class... supportedTypes) {
        // TODO!! cache would not work in isChildMethod, also think about merging with yafFields!!!
        Map<String, Field> fields = new HashMap<>();
        ReflectionUtils.doWithFields(cls, field -> {
            field.setAccessible(true);
            fields.put(field.getName(), field);
        }, field -> isChild(field.getType(), supportedTypes));
        return fields;
    }

    /**
     * Contains bean boolean.
     *
     * @param name the name
     * @return the boolean
     */
    @Cacheable(value = "beanNames", key = "#name")
    public boolean containsBean(String name) {
        return applicationContext.containsBeanDefinition(name);
    }

    /**
     * Is child boolean.
     *
     * @param child   the child
     * @param parents the parents
     * @return the boolean
     */
    @Cacheable(value = "classChildren", key = "{#child, #parents}")
    public boolean isChild(Class child, Class... parents) {
        return YafReflectionUtils.isChild(child, parents);
    }

    /**
     * Gets annotation.
     *
     * @param <T>             the type parameter
     * @param e               the e
     * @param annotationClass the annotation class
     * @return the annotation
     */
    @Cacheable(value = "annotation", key = "{#e, #annotationClass}")
    public <T extends Annotation> T getAnnotation(AnnotatedElement e, Class annotationClass) {
        return YafReflectionUtils.getAnnotation(e, annotationClass);
    }

    /**
     * Gets annotations.
     *
     * @param annotationClass the annotation class
     * @return the annotations
     */
    @Cacheable(value = "annotation", key = "{#annotationClass}")
    public List<Annotation> getAnnotations(Class annotationClass) {
        return YafReflectionUtils.getAnnotations(annotationClass);
    }

    private boolean isSupportedBean(Class<?> objType) {
        return supportedClasses.stream().anyMatch(c -> c.isAssignableFrom(objType));
    }

    /**
     * Gets bean.
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return the bean
     */
    public <T extends Object> T getBean(Class cls) {
        return (T) applicationContext.getBean(cls);
    }

    /**
     * Gets yaf managed bean.
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return the yaf managed bean
     */
    public <T extends Object> T getYafManagedBean(Class cls) {
        return getYafManagedBeanFromField(null, cls, null, null, Collections.emptyList());
    }

    /**
     * Gets yaf managed bean from field.
     *
     * @param <T>         the type parameter
     * @param field       the field
     * @param fieldType   the field type
     * @param obj         the obj
     * @param objType     the obj type
     * @param annotations the annotations
     * @return the yaf managed bean from field
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> T getYafManagedBeanFromField(Field field, Class fieldType, Object obj, Class objType, List<Annotation> annotations) {
        TestExecutionContext testExecutionContext = applicationContext.getBean(TestExecutionContext.class);

        Map<String, ?> beansOfType = applicationContext.getBeansOfType(fieldType, true, true);
        T bean;
        if (beansOfType.size() == 1) {
            // only one possible impl
            bean = (T) beansOfType.get(beansOfType.keySet().stream().findFirst().get());
        } else {
            // find proper bean according env
            Environment env = testExecutionContext.getEnv();

            bean = (T) beansOfType.values().parallelStream()
                    .filter(x-> !YafReflectionUtils.isInstantiable(fieldType) || x.getClass().equals(fieldType))
                    .collect(Collectors.toMap(p -> p,
                            p -> matchService.matches(p.getClass(), env, testExecutionContext, this)))
                    .entrySet().parallelStream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .filter(e -> e.getValue() >= 0)
                    .map(Map.Entry::getKey).findFirst()
                    .orElseThrow(() -> new EnvSetupYafException("Unable to init " + objType.getCanonicalName() + " due multiple variants!"));
        }
        if (bean != null) {
            // process bean
            List<Annotation> fieldAnnotations = new ArrayList<>();
            if (field != null) {
                fieldAnnotations = Arrays.asList(field.getDeclaredAnnotations());
            }
            YafBeanProcessor beanProcessor = applicationContext.getBean(YafBeanProcessor.class);
            beanProcessor.processBean(bean, fieldAnnotations, testExecutionContext);
            if (bean instanceof com.coherentsolutions.yaf.core.pom.Component) {
                ((com.coherentsolutions.yaf.core.pom.Component) bean).configureComponent();
            }
            if (bean instanceof IContextual) {
                ((IContextual) bean).setTestExecutionContext(testExecutionContext, field, obj, objType, fieldAnnotations);
            }
        }
        return bean;
    }

    /**
     * Tec test execution context.
     *
     * @return the test execution context
     */
    public TestExecutionContext tec() {
        try {
            return getBean(TestExecutionContext.class);
        } catch (Exception ex) {
            return null;
        }
    }
}