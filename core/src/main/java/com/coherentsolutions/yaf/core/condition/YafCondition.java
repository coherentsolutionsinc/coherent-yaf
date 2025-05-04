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

package com.coherentsolutions.yaf.core.condition;


import com.coherentsolutions.yaf.core.enums.Browser;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.enums.MobileOS;
import com.coherentsolutions.yaf.core.enums.OS;

import java.lang.annotation.*;

/**
 * The interface Yaf condition.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface YafCondition {

    /**
     * Device type device type.
     *
     * @return the device type
     */
    DeviceType deviceType() default DeviceType.OTHER;

    /**
     * Os os.
     *
     * @return the os
     */
    OS os() default OS.OTHER;

    /**
     * Mobile os mobile os.
     *
     * @return the mobile os
     */
    MobileOS mobileOs() default MobileOS.OTHER;

    /**
     * Mobile os version string.
     *
     * @return the string
     */
    String mobileOsVersion() default "";

    /**
     * Browser browser.
     *
     * @return the browser
     */
    Browser browser() default Browser.OTHER;

    /**
     * Browser version string.
     *
     * @return the string
     */
    String browserVersion() default "";

    /**
     * App version string.
     *
     * @return the string
     */
    String appVersion() default "";

    /**
     * Os version string.
     *
     * @return the string
     */
    String osVersion() default "";

    /**
     * Width int.
     *
     * @return the int
     */
    int width() default 0;

    /**
     * Height int.
     *
     * @return the int
     */
    int height() default 0;

    /**
     * Simulator boolean.
     *
     * @return the boolean
     */
    boolean simulator() default false;

    /**
     * Matcher class.
     *
     * @return the class
     */
    Class matcher() default BaseConditionMatcher.class;

    // TODO other props

}
