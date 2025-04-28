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

package com.coherentsolutions.yaf.web.wait.driver.waits.presence;

import com.coherentsolutions.yaf.web.wait.driver.WaitConsts;
import com.coherentsolutions.yaf.web.wait.driver.WaitFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation provides custom wait conditions for checking the presence of web elements in the DOM.
 * <p>
 * The annotation can be used to configure wait conditions for web elements to be present or absent in the DOM,
 * with options to handle any or all elements matching the criteria.
 * </p>
 * <p>
 * The annotation supports the use of constants to configure the wait conditions and can be
 * used with Selenium WebDriver to facilitate more complex wait conditions that are not
 * provided by default.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@WaitFor()
public @interface WaitForPresence {

    /**
     * Presence boolean.
     *
     * @return the boolean
     */
    boolean presence() default true;

    /**
     * Any element match boolean.
     *
     * @return the boolean
     */
    boolean anyElementMatch() default true;

    /**
     * Is for load boolean.
     *
     * @return the boolean
     */
    boolean isForLoad() default false;

    /**
     * Wait const wait consts.
     *
     * @return the wait consts
     */
    WaitConsts waitConst() default WaitConsts.EMPTY;
}
