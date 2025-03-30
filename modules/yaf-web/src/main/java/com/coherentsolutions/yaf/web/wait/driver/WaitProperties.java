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

package com.coherentsolutions.yaf.web.wait.driver;

import com.coherentsolutions.yaf.core.config.ModuleProperties;
import com.coherentsolutions.yaf.core.consts.Consts;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * The type Wait properties.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = Consts.FRAMEWORK_NAME + ".wait")
public class WaitProperties extends ModuleProperties {

    /**
     * The Set implicit.
     */
    boolean setImplicit = false;
    /**
     * The Wait multiplication.
     */
    int waitMultiplication = 1;

    /**
     * The Implicit.
     */
    ImplicitProperties implicit = new ImplicitProperties();
    /**
     * The Explicit.
     */
    ExplicitProperties explicit = new ExplicitProperties();

    /**
     * The type Implicit properties.
     */
    @Data
    @Configuration
    @ConfigurationProperties(prefix = Consts.FRAMEWORK_NAME + ".wait.implicit")
    public static class ImplicitProperties {

        /**
         * The Time out.
         */
        Integer timeOut;
        /**
         * The Page load.
         */
        Integer pageLoad;
        /**
         * The Script.
         */
        Integer script;
        /**
         * The Time unit.
         */
        TimeUnit timeUnit = TimeUnit.SECONDS;

    }

    /**
     * The type Explicit properties.
     */
    @Data
    @Configuration
    @ConfigurationProperties(prefix = Consts.FRAMEWORK_NAME + ".wait.explicit")
    public static class ExplicitProperties {

        /**
         * The Time out.
         */
        int timeOut = 3;
        /**
         * The Interval.
         */
        int interval = 200;
    }

}
