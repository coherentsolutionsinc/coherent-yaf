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

package com.coherentsolutions.yaf.web.pom.by;


import java.lang.annotation.*;

/**
 * Annotation to define a selector for a {@link com.coherentsolutions.yaf.web.pom.WebComponent} field.
 * <p>
 * This annotation is used to specify a selector for a field in a web component.
 * It allows for the customization of the selector type and value, and can be
 * applied to fields to indicate how they should be located within the web component.
 * </p>
 * <ul>
 *   <li>{@code field} - The name of the field to which the selector applies.</li>
 *   <li>{@code value} - The selector value, such as a CSS selector, XPath expression, etc.</li>
 *   <li>{@code type} - The type of the selector, which can be CSS, XPath, etc. Defaults to CSS.</li>
 * </ul>
 * <p>
 * This annotation is repeatable and can be inherited by subclasses.
 * </p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(YafSet.class)
@Inherited
@Documented
public @interface YafSetSelector {

    String field();

    String value() default "";

    SelectorType type() default SelectorType.CSS;
}
