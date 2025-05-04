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

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

/**
 * The type Yaf annotation utils.
 */
@Service
public class YafAnnotationUtils {


    /**
     * Gets annotation.
     *
     * @param <T>             the type parameter
     * @param e               the e
     * @param annotationClass the annotation class
     * @return the annotation
     */
    @Cacheable(value = "yafElementAnnotation", key = "{#e, #annotationClass}")
    public <T extends Annotation> T getAnnotation(AnnotatedElement e, Class annotationClass) {
        return YafReflectionUtils.getAnnotation(e, annotationClass);
    }

    /**
     * Gets annotations.
     *
     * @param cls the cls
     * @return the annotations
     */
    @Cacheable(value = "yafClassAnnotations", key = "{#cls}")
    public List<Annotation> getAnnotations(Class cls) {
        return YafReflectionUtils.getAnnotations(cls);
    }

    /**
     * Annotation exists boolean.
     *
     * @param e               the e
     * @param annotationClass the annotation class
     * @return the boolean
     */
    @Cacheable(value = "yafAnnotationExists", key = "{#e, #annotationClass}")
    public boolean annotationExists(AnnotatedElement e, Class annotationClass) {
        return YafReflectionUtils.getAnnotation(e, annotationClass) != null;
    }
}
