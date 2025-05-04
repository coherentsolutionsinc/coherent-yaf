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

package com.coherentsolutions.yaf.core.test;


import com.coherentsolutions.yaf.core.enums.Severity;

import java.lang.annotation.*;

/**
 * The interface Yaf test.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface YafTest {

    /**
     * Name string.
     *
     * @return the string
     */
    String name() default "";

    /**
     * Description string.
     *
     * @return the string
     */
    String description() default "";

    /**
     * Tms ids string [ ].
     *
     * @return the string [ ]
     */
    String[] tmsIds() default {};

    /**
     * Bugs string [ ].
     *
     * @return the string [ ]
     */
    String[] bugs() default {};

    /**
     * Severity severity.
     *
     * @return the severity
     */
    Severity severity() default Severity.UNKNOWN;

    /**
     * Data file string.
     *
     * @return the string
     */
    String dataFile() default "";
    // todo others
}
