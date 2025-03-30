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


import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Yaf reflection utils.
 */
public class YafReflectionUtils {

    /**
     * Change annotation value object.
     *
     * @param annotation the annotation
     * @param key        the key
     * @param newValue   the new value
     * @return the object
     */
    public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue) {
        Object handler = Proxy.getInvocationHandler(annotation);
        Field f;
        try {
            f = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
        f.setAccessible(true);
        Map<String, Object> memberValues;
        try {
            memberValues = (Map<String, Object>) f.get(handler);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        Object oldValue = memberValues.get(key);
        if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
            throw new IllegalArgumentException();
        }
        memberValues.put(key, newValue);
        return oldValue;
    }

    /**
     * Sets field value.
     *
     * @param fieldName  the field name
     * @param fieldValue the field value
     * @param obj        the obj
     */
    public static void setFieldValue(String fieldName, Object fieldValue, Object obj) {
        try {
            Field field = obj.getClass().getField(fieldName);
            setFieldValue(field, fieldValue, obj);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sets field value.
     *
     * @param field      the field
     * @param fieldValue the field value
     * @param obj        the obj
     */
    public static void setFieldValue(Field field, Object fieldValue, Object obj) {
        try {
            field.setAccessible(true);
            field.set(obj, fieldValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Gets annotations.
     *
     * @param e the e
     * @return the annotations
     */
    public static List<Annotation> getAnnotations(AnnotatedElement e) {
        return Arrays.stream(e.getDeclaredAnnotations()).collect(Collectors.toList());
    }

    /**
     * Gets annotation.
     *
     * @param <T>           the type parameter
     * @param e             the e
     * @param annotationCls the annotation cls
     * @return the annotation
     */
    public static <T extends Annotation> T getAnnotation(AnnotatedElement e, Class annotationCls) {
        return (T) AnnotationUtils.getAnnotation(e, annotationCls);
    }

    /**
     * Find annotation t.
     *
     * @param <T>         the type parameter
     * @param annotations the annotations
     * @param cls         the cls
     * @return the t
     */
    public static <T extends Annotation> T findAnnotation(List<Annotation> annotations, Class cls) {
        return (T) annotations.stream().filter(a -> a.annotationType().equals(cls)).findFirst().orElse(null);
    }

    /**
     * Is child boolean.
     *
     * @param child   the child
     * @param parents the parents
     * @return the boolean
     */
    public static boolean isChild(Class child, List<Class> parents) {
        return parents.stream().filter(p -> p.isAssignableFrom(child)).findAny().isPresent();
    }

    /**
     * Is child boolean.
     *
     * @param child   the child
     * @param parents the parents
     * @return the boolean
     */
    public static boolean isChild(Class child, Class... parents) {
        if (parents.length > 0) {
            return isChild(child, Arrays.asList(parents));
        } else {
            return false;
        }
    }

    /**
     * Gets all annotations by type.
     *
     * @param <T>                the type parameter
     * @param e                  the e
     * @param baseAnnotation     the base annotation
     * @param repeaterAnnotation the repeater annotation
     * @return the all annotations by type
     */
    public static <T extends Annotation> List<T> getAllAnnotationsByType(AnnotatedElement e, Class baseAnnotation,
                                                                         Class repeaterAnnotation) {
        return getAllAnnotationsByType(getAnnotations(e), baseAnnotation, repeaterAnnotation);
    }

    /**
     * Gets all annotations by type.
     *
     * @param <T>            the type parameter
     * @param e              the e
     * @param baseAnnotation the base annotation
     * @return the all annotations by type
     */
    public static <T extends Annotation> List<T> getAllAnnotationsByType(AnnotatedElement e, Class baseAnnotation) {
        return getAllAnnotationsByType(getAnnotations(e), baseAnnotation);
    }

    /**
     * Gets all annotations by type.
     *
     * @param <T>            the type parameter
     * @param annotations    the annotations
     * @param baseAnnotation the base annotation
     * @return the all annotations by type
     */
    public static <T extends Annotation> List<T> getAllAnnotationsByType(List<Annotation> annotations,
                                                                         Class baseAnnotation) {
        Repeatable repeatable = AnnotationUtils.getAnnotation(baseAnnotation, Repeatable.class);
        if (repeatable != null) {
            return getAllAnnotationsByType(annotations, baseAnnotation, repeatable.value());
        }
        return getAllAnnotationsByType(annotations, baseAnnotation, null);
    }

    /**
     * Gets all annotations by type.
     *
     * @param <T>                the type parameter
     * @param annotations        the annotations
     * @param baseAnnotation     the base annotation
     * @param repeaterAnnotation the repeater annotation
     * @return the all annotations by type
     */
    public static <T extends Annotation> List<T> getAllAnnotationsByType(List<Annotation> annotations,
                                                                         Class baseAnnotation, Class repeaterAnnotation) {
        List<Annotation> annotationList = annotations.stream().filter(a -> a.annotationType().equals(baseAnnotation))
                .collect(Collectors.toList());
        if (repeaterAnnotation != null) {
            Annotation repeatedAnnotation = annotations.stream()
                    .filter(a -> a.annotationType().equals(repeaterAnnotation)).findFirst().orElse(null);
            if (repeatedAnnotation != null) {
                Annotation[] rValue = (Annotation[]) AnnotationUtils.getAnnotationAttributes(repeatedAnnotation)
                        .get("value");
                annotationList.addAll(Arrays.asList(rValue));
            }
        }
        return (List<T>) annotationList;
    }

    /**
     * Determines if the specified class type is instantiable.
     * <p>
     * This method checks if the given class type is not an interface and not an abstract class,
     * indicating that instances of the class can be created.
     * </p>
     *
     * @param fieldType the class type to check
     * @return {@code true} if the class type is instantiable, {@code false} otherwise
     */
    public static boolean isInstantiable(Class<?> fieldType) {
        return !fieldType.isInterface() && !Modifier.isAbstract(fieldType.getModifiers());
    }
}
